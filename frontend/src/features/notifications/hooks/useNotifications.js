import { useEffect, useState, useCallback, useRef } from 'react';
import { connectNotificationsSocket, disconnectNotificationsSocket, subscribeUnread, setUnread, decrementUnread } from '@/features/notifications/services/notificationSocket';
import { getNotifications, getUnreadCount, markNotificationRead, markAllNotificationsRead } from '../services/notificationsService';
import notificationIcon from '@/assets/stockia.svg';

export function useNotifications() {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCountLocal] = useState(0);
  const [loading, setLoading] = useState(false);
  const permissionRequestedRef = useRef(false);
  // keep ref to latest notifications for synchronous access
  const notificationsRef = useRef([]);
  // low-stock count ref to aggregate desktop notifications
  const lowStockCountRef = useRef(0);

  const recomputeLowStockCount = useCallback((list) => {
    const arr = list ?? notificationsRef.current;
    const cnt = (arr || []).filter(n => n?.type === 'LOW_STOCK' && !n?.isRead).length;
    lowStockCountRef.current = cnt;
    return cnt;
  }, []);

  // last unread value shown in a desktop notification
  const lastShownUnreadRef = useRef(null);
  // try to restore last shown unread from sessionStorage to avoid duplicates across reloads
  try {
    const saved = sessionStorage.getItem('notifications:lastShownUnread');
    if (saved !== null && saved !== undefined) lastShownUnreadRef.current = Number(saved);
  } catch (e) {
    // ignore storage errors
  }
  // value currently being processed to show (prevent concurrent shows)
  const pendingShowRef = useRef(null);
  // throttle API confirm calls
  const lastApiCheckRef = useRef(0);
  const API_CHECK_INTERVAL = 5000; // ms

  const showUnreadNotificationIfChanged = useCallback(async (count) => {
    try {
      if (typeof window === 'undefined' || !('Notification' in window)) return;
      const num = Number(count || 0);
      // If there are no unread notifications, do not show a desktop notification.
      if (num === 0) return;
      // if already showing/processing this value, skip
      if (pendingShowRef.current === num) return;

      // if we already showed this exact number previously, we may still need to confirm with API
      if (lastShownUnreadRef.current === num) {
        const now = Date.now();
        if (now - lastApiCheckRef.current < API_CHECK_INTERVAL) return; // recently checked, skip
        // throttle API checks
        lastApiCheckRef.current = now;
        try {
          const serverRes = await getUnreadCount();
          const serverCount = typeof serverRes === 'number' ? serverRes : serverRes?.data ?? serverRes ?? 0;
          if (Number(serverCount) === num) return; // confirmed no change, skip
          // else proceed and show new value
        } catch (e) {
          // on API failure, be conservative and skip showing to avoid duplicates
          return;
        }
      }

      // mark pending immediately to block concurrent calls
      pendingShowRef.current = num;

      // request permission if needed (ensure we set requested flag before awaiting)
      if (Notification.permission === 'default' && !permissionRequestedRef.current) {
        permissionRequestedRef.current = true;
        const perm = await Notification.requestPermission();
        if (perm !== 'granted') { pendingShowRef.current = null; return; }
      }

      if (Notification.permission !== 'granted') { pendingShowRef.current = null; return; }

      const title = 'Alerta, stock bajo!';
      const body = num === 0 ? 'No tienes notificaciones sin leer.' : `Tienes ${num} productos en stock bajo. Fsvor revisar lista de productos`;
      const notif = new Notification(title, { body, icon: notificationIcon });
      notif.onclick = () => {
        try { window.focus(); } catch (e) {}
        try { window.location.href = '/products'; } catch (e) {}
      };

      lastShownUnreadRef.current = num;
      try { sessionStorage.setItem('notifications:lastShownUnread', String(num)); } catch (e) { /* ignore */ }
      pendingShowRef.current = null;
    } catch (err) {
      console.error('Failed to show unread notification', err);
    }
  }, []);

  const handleIncoming = useCallback((notification) => {
    // Update notifications state and compute low-stock aggregation in the same functional update
    setNotifications(prev => {
      const newArr = [notification, ...prev];
      // update ref
      notificationsRef.current = newArr;
      // recompute low-stock count (still useful for other UI)
      recomputeLowStockCount(newArr);
      return newArr;
    });
    // we rely on subscribeUnread to receive updated unread count and trigger desktop notification if changed
  }, [recomputeLowStockCount]);

  const fetchUnread = useCallback(async () => {
    try {
      const c = await getUnreadCount();
      const count = typeof c === 'number' ? c : c?.data ?? c ?? 0;
      setUnreadCountLocal(Number(count));
      setUnread(Number(count));
      // show desktop notification if unread changed
      showUnreadNotificationIfChanged(Number(count));
      return count;
    } catch (e) {
      console.error('Failed to fetch unread count', e);
      return 0;
    }
  }, [showUnreadNotificationIfChanged]);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getNotifications();
      const content = res?.content ?? res ?? [];
      setNotifications(content);
      notificationsRef.current = content;
      // recompute low stock count after full list fetched
      recomputeLowStockCount(content);
      // refresh unread and possibly show notification if changed
      try { await fetchUnread(); } catch (e) { /* ignore */ }
    } catch (e) {
      console.error('Failed to fetch notifications list', e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let unsubUnread = null;

    // connect websocket
    connectNotificationsSocket(handleIncoming);

    // subscribe to realtime unread updates
    unsubUnread = subscribeUnread((count) => {
      console.debug('[useNotifications] unread event ->', count);
      setUnreadCountLocal(Number(count) || 0);
      // show desktop notification if unread changed
      showUnreadNotificationIfChanged(Number(count));
    });

    // fetch initial data on page load: list + unread
    (async () => {
      try {
        await fetchList();
      } catch (e) {
        console.error('Failed to fetch notifications on mount', e);
      }
      try {
        const c = await getUnreadCount();
        const count = typeof c === 'number' ? c : c?.data ?? c ?? 0;
        setUnreadCountLocal(Number(count));
        // ensure shared socket state is aligned
        setUnread(Number(count));
      } catch (e) {
        console.error('Failed to fetch unread count', e);
      }
    })();

    return () => {
      if (unsubUnread) unsubUnread();
      disconnectNotificationsSocket();
    };
  }, [fetchList, fetchUnread, handleIncoming]);

  const markRead = useCallback(async (id) => {
    try {
      await markNotificationRead(id);
      setNotifications(prev => prev.map(n => n.id === id ? { ...n, isRead: true } : n));
      // decrement shared unread and local
      decrementUnread(1);
      setUnreadCountLocal(u => Math.max(u - 1, 0));
      // update ref and recompute low-stock
      setTimeout(() => {
        notificationsRef.current = notificationsRef.current.map(n => n.id === id ? { ...n, isRead: true } : n);
        recomputeLowStockCount();
        // after marking read, refresh unread count from server to keep UI and desktop notification in sync
        try { fetchUnread(); } catch (e) { /* ignore */ }
      }, 0);
    } catch (e) {
      console.error('Failed to mark notification read', e);
    }
  }, [fetchUnread, recomputeLowStockCount]);

  const markAllRead = useCallback(async () => {
    try {
      await markAllNotificationsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      setUnread(0);
      setUnreadCountLocal(0);
      // update ref and reset low-stock
      notificationsRef.current = (notificationsRef.current || []).map(n => ({ ...n, isRead: true }));
      recomputeLowStockCount();
      // refresh unread from server (ensures desktop notification shows 0 if changed)
      try { fetchUnread(); } catch (e) { /* ignore */ }
    } catch (e) {
      console.error('Failed to mark all read', e);
    }
  }, [fetchUnread, recomputeLowStockCount]);

  return {
    notifications,
    unreadCount,
    loading,
    fetchList,
    fetchUnread,
    markRead,
    markAllRead,
    showLowStockNotification: showUnreadNotificationIfChanged,
  };
}
