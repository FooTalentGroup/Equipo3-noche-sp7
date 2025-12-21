import apiClient from '@/shared/services/apiClient.js';

const unwrap = (res) => res?.data?.data ?? res?.data ?? res;

export async function getUsers({ page = 0, size = 20, name = '' } = {}) {
  const params = { page, size };
  if (name) params.name = name;
  const res = await apiClient.get('/api/users', { params });
  const data = unwrap(res);

  const content = data?.content ?? data?.users ?? (Array.isArray(data) ? data : []);

     return {
    users: content,
    totalPages: data?.totalPages ?? data?.page?.totalPages ?? 1,
    totalElements: data?.totalElements ?? data?.page?.totalElements ?? (content.length ?? 0),
    pageSize: size,
    pageNumber: data?.pageable?.pageNumber ?? data?.number ?? page,
  };
}

export async function getUser(id) {
  const res = await apiClient.get(`/api/users/${id}`);
  return unwrap(res);
}

export async function createUser(payload) {
  const body = {
    name: payload.nombre ?? payload.name,
    email: payload.email,
    password: payload.password,
    role: payload.role,
    accountStatus: payload.accountStatus ?? 'ACTIVE',
  };
  const res = await apiClient.post('/api/auth/register', body);
  return unwrap(res);
}

export async function updateUser(id, payload, activateUser = false) {
  const body = activateUser ? {
    accountStatus: 'ACTIVE',
  } :
      {
    name: payload.nombre ?? payload.name,
    email: payload.email,
    ...(payload.password ? { password: payload.password } : {}),
    role: payload.role,
    accountStatus: payload.accountStatus ?? 'ACTIVE',
  };
  const res = await apiClient.put(`/api/users/${id}`, body);
  return unwrap(res);
}

export async function deleteUser(id) {
  const body = {
    deleted: true,
    accountStatus: 'INACTIVE',
  };
  const res = await apiClient.put(`/api/users/${id}`, body);
  return unwrap(res);
}
