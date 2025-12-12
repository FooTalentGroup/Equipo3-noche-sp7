import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { getAuthToken } from '@/features/auth/utils/authStorage';
import { getUnreadCount as apiGetUnreadCount } from '../services/notificationsService';

let stompClient = null;
let unreadCount = 0;

// Simple emitter
const listeners = new Map();
function on(event, cb) {
  if (!listeners.has(event)) listeners.set(event, new Set());
  listeners.get(event).add(cb);
  return () => off(event, cb);
}
function off(event, cb) {
  if (!listeners.has(event)) return;
  listeners.get(event).delete(cb);
}
function emit(event, payload) {
  if (!listeners.has(event)) return;
  if (event === 'unread') console.debug('[notificationSocket] emit unread ->', payload);
  for (const cb of Array.from(listeners.get(event))) {
    try { cb(payload); } catch (e) { console.error('emitter callback error', e); }
  }
}

export const connectNotificationsSocket = async (onNotification) => {
  if (stompClient && stompClient.connected) return;
  const wsUrl = import.meta.env.VITE_WS_URL ?? '/ws';
  const socket = new SockJS(wsUrl);
  stompClient = Stomp.over(socket);

  const token = getAuthToken();
  const headers = token ? { Authorization: `Bearer ${token}` } : {};

  try {
    stompClient.connect(headers, async () => {
      try {
        stompClient.subscribe('/topic/notifications', (message) => {
          try {
            const notification = JSON.parse(message.body);
            onNotification?.(notification);
            unreadCount = Number(unreadCount || 0) + 1;
            emit('unread', unreadCount);
            emit('message', notification);
          } catch (err) {
            console.error('Failed to parse notification message', err);
          }
        });
      } catch (subErr) {
        console.error('Subscribe error:', subErr);
      }

      try {
        const c = await apiGetUnreadCount();
        const count = typeof c === 'number' ? c : c?.data ?? c ?? 0;
        unreadCount = Number(count) || 0;
        emit('unread', unreadCount);
      } catch (e) {
        console.error('Failed to fetch initial unread count', e);
      }
    }, (err) => {
      console.error('STOMP connect error:', err);
    });
  } catch (e) {
    console.error('Failed to initialize STOMP client', e);
  }
};

export const disconnectNotificationsSocket = () => {
  try {
    if (stompClient) {
      stompClient.disconnect(() => {
      });
      stompClient = null;
    }
  } catch (e) {
  }
};

// Subscription helper for unread count
export const subscribeUnread = (cb) => on('unread', cb);

// Helpers to update unread from outside (e.g., after marking read via REST)
export const setUnread = (value) => {
  unreadCount = Number(value) || 0;
  emit('unread', unreadCount);
};
export const decrementUnread = (by = 1) => {
  unreadCount = Math.max(0, Number(unreadCount || 0) - (Number(by) || 1));
  emit('unread', unreadCount);
};
