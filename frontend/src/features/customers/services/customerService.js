import apiClient from '@/shared/services/apiClient.js';

const unwrap = (response) => response?.data ?? response;

export const getClients = async () => {
  const { data } = await apiClient.get('/api/clients');
  const payload = unwrap(data);
  return payload?.data ?? payload ?? [];
};

export const getClientById = async (id) => {
  const { data } = await apiClient.get(`/api/clients/${id}`);
  const payload = unwrap(data);
  return payload?.data ?? payload ?? null;
};

export const createClient = async (clientData) => {
  const { data } = await apiClient.post('/api/clients', clientData);
  const payload = unwrap(data);
  return payload?.data ?? payload ?? null;
};
