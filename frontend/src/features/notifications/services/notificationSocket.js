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
  if (event === 'unread') {
    // keep dev debug but less noisy
    try { console.debug('[notificationSocket] emit unread ->', payload); } catch (e) { /* ignore */ }
  }
  for (const cb of Array.from(listeners.get(event))) {
    try { cb(payload); } catch (e) { /* swallow listener errors */ }
  }
}

const isDev = typeof import.meta !== 'undefined' && import.meta.env && import.meta.env.MODE === 'development';
function logError(...args) {
  // Silence socket-related errors in development to avoid noisy logs when backend WS isn't available.
  if (!isDev) console.error(...args);
}
function logDebug(...args) {
  if (isDev) console.debug(...args); else console.debug(...args);
}

export const connectNotificationsSocket = async (onNotification) => {
  try {
    if (stompClient && stompClient.connected) return;

    const wsUrl = import.meta.env.VITE_WS_URL ?? '';

    // In local dev (vite) the dev server does not provide the SockJS endpoints used by backend.
    // Skip attempting a connection in common dev setups to avoid noisy 404 / WebSocket errors.
    const runningOnViteLocalhost = typeof window !== 'undefined' && window.location && window.location.hostname === 'localhost' && window.location.port === '5173';
    const noWsConfigured = !wsUrl || wsUrl === '/ws';
    if (isDev && runningOnViteLocalhost && noWsConfigured) {
      logDebug('[notificationSocket] WebSocket connection skipped in dev (no VITE_WS_URL configured)');
      return;
    }

    const socket = new SockJS(wsUrl);
    // Use Stomp.over only when we have a socket; if it throws we'll catch below
    stompClient = Stomp.over(socket);

    const token = getAuthToken();
    const headers = token ? { Authorization: `Bearer ${token}` } : {};

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
            // log only in non-dev
            logError('Failed to parse notification message', err);
          }
        });
      } catch (subErr) {
        logError('Subscribe error:', subErr);
      }

      try {
        const c = await apiGetUnreadCount();
        const count = typeof c === 'number' ? c : c?.data ?? c ?? 0;
        unreadCount = Number(count) || 0;
        emit('unread', unreadCount);
      } catch (e) {
        logError('Failed to fetch initial unread count', e);
      }
    }, (err) => {
      logError('STOMP connect error:', err);
    });
  } catch (e) {
    logError('Failed to initialize STOMP client', e);
  }
};

export const disconnectNotificationsSocket = () => {
  try {
    if (stompClient) {
      try { stompClient.disconnect(() => {}); } catch (e) { /* swallow */ }
      stompClient = null;
    }
  } catch (e) {
  }
};

export const subscribeUnread = (cb) => on('unread', cb);

export const setUnread = (value) => {
  unreadCount = Number(value) || 0;
  emit('unread', unreadCount);
};
export const decrementUnread = (by = 1) => {
  unreadCount = Math.max(0, Number(unreadCount || 0) - (Number(by) || 1));
  emit('unread', unreadCount);
};
