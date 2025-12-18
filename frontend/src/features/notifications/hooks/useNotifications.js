import { useEffect, useState, useCallback, useRef } from 'react';
import { connectNotificationsSocket, disconnectNotificationsSocket, subscribeUnread, setUnread, decrementUnread } from '@/features/notifications/services/notificationSocket';
import { getNotifications, getUnreadCount, markNotificationRead, markAllNotificationsRead } from '../services/notificationsService';
import notificationIcon from '@/assets/stockia.svg';

export function useNotifications() {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCountLocal] = useState(0);
  const [loading, setLoading] = useState(false);
  const permissionRequestedRef = useRef(false);
  const notificationsRef = useRef([]);
  const lowStockCountRef = useRef(0);

  const recomputeLowStockCount = useCallback((list) => {
    const arr = list ?? notificationsRef.current;
    const cnt = (arr || []).filter(n => n?.type === 'LOW_STOCK' && !n?.isRead).length;
    lowStockCountRef.current = cnt;
    return cnt;
  }, []);

  const lastShownUnreadRef = useRef(null);
  try {
    const saved = sessionStorage.getItem('notifications:lastShownUnread');
    if (saved !== null && saved !== undefined) lastShownUnreadRef.current = Number(saved);
  } catch (e) {
  }
  const pendingShowRef = useRef(null);
  const lastApiCheckRef = useRef(0);
  const API_CHECK_INTERVAL = 5000; 

  const showUnreadNotificationIfChanged = useCallback(async (count) => {
    try {
      if (typeof window === 'undefined' || !('Notification' in window)) return;
      const num = Number(count || 0);
      if (num === 0) return;
      if (pendingShowRef.current === num) return;

      if (lastShownUnreadRef.current === num) {
        const now = Date.now();
        if (now - lastApiCheckRef.current < API_CHECK_INTERVAL) return;
        lastApiCheckRef.current = now;
        try {
          const serverRes = await getUnreadCount();
          const serverCount = typeof serverRes === 'number' ? serverRes : serverRes?.data ?? serverRes ?? 0;
          if (Number(serverCount) === num) return; 
        } catch (e) {
          return;
        }
      }

      pendingShowRef.current = num;

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
    setNotifications(prev => {
      const newArr = [notification, ...prev];
      notificationsRef.current = newArr;
      recomputeLowStockCount(newArr);
      return newArr;
    });
  }, [recomputeLowStockCount]);

  const fetchUnread = useCallback(async () => {
    try {
      const c = await getUnreadCount();
      const count = typeof c === 'number' ? c : c?.data ?? c ?? 0;
      setUnreadCountLocal(Number(count));
      setUnread(Number(count));
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
      recomputeLowStockCount(content);
      try { await fetchUnread(); } catch (e) { /* ignore */ }
    } catch (e) {
      console.error('Failed to fetch notifications list', e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let unsubUnread = null;

    connectNotificationsSocket(handleIncoming);

    unsubUnread = subscribeUnread((count) => {
      console.debug('[useNotifications] unread event ->', count);
      setUnreadCountLocal(Number(count) || 0);
      showUnreadNotificationIfChanged(Number(count));
    });

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
      decrementUnread(1);
      setUnreadCountLocal(u => Math.max(u - 1, 0));
      setTimeout(() => {
        notificationsRef.current = notificationsRef.current.map(n => n.id === id ? { ...n, isRead: true } : n);
        recomputeLowStockCount();
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
      notificationsRef.current = (notificationsRef.current || []).map(n => ({ ...n, isRead: true }));
      recomputeLowStockCount();
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
