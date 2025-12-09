import apiClient from '@/shared/services/apiClient.js';

const unwrap = (res) => res?.data?.data ?? res?.data ?? res;

export async function getNotifications({ page = 0, size = 20, type, isRead } = {}) {
  const params = { page, size };
  if (type) params.type = type;
  if (typeof isRead !== 'undefined') params.isRead = isRead;
  const res = await apiClient.get('/api/notifications', { params });
  const data = unwrap(res);
  return data;
}

export async function getUnreadCount() {
  const res = await apiClient.get('/api/notifications/unread-count');
  const data = unwrap(res);
  // some responses wrap the count in data, others return { data: count }
  return typeof data === 'number' ? data : data?.data ?? data;
}

export async function markNotificationRead(id) {
  const res = await apiClient.put(`/api/notifications/${id}/read`);
  return unwrap(res);
}

export async function markAllNotificationsRead() {
  const res = await apiClient.put('/api/notifications/read-all');
  return unwrap(res);
}

