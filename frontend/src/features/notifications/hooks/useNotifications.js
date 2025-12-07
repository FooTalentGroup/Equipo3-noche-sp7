import { useEffect, useState, useCallback } from 'react';
import { connectNotificationsSocket, disconnectNotificationsSocket, subscribeUnread, setUnread, decrementUnread } from '@/features/notifications/services/notificationSocket';
import { getNotifications, getUnreadCount, markNotificationRead, markAllNotificationsRead } from '../services/notificationsService';

export function useNotifications() {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCountLocal] = useState(0);
  const [loading, setLoading] = useState(false);

  const handleIncoming = useCallback((notification) => {
    setNotifications(prev => [notification, ...prev]);
    // real-time increment will be emitted by socket; local increment happens via subscription
  }, []);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getNotifications();
      const content = res?.content ?? res ?? [];
      setNotifications(content);
    } catch (e) {
      console.error('Failed to fetch notifications list', e);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchUnread = useCallback(async () => {
    try {
      const c = await getUnreadCount();
      const count = typeof c === 'number' ? c : c?.data ?? c ?? 0;
      setUnreadCountLocal(Number(count));
      setUnread(Number(count));
    } catch (e) {
      console.error('Failed to fetch unread count', e);
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
    } catch (e) {
      console.error('Failed to mark notification read', e);
    }
  }, []);

  const markAllRead = useCallback(async () => {
    try {
      await markAllNotificationsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      setUnread(0);
      setUnreadCountLocal(0);
    } catch (e) {
      console.error('Failed to mark all read', e);
    }
  }, []);

  return {
    notifications,
    unreadCount,
    loading,
    fetchList,
    fetchUnread,
    markRead,
    markAllRead,
  };
}
