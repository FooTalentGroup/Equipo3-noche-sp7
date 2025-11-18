import apiClient from '@/shared/services/apiClient.js';

export const login = async (credentials) => {
  const response = await apiClient.post('/api/auth/login', credentials);
  return response.data;
};

export const register = async (userData) => {
  const { data } = await apiClient.post('/api/auth/register', userData);
  return data;
};

export const logout = async () => {
  await apiClient.post('/api/auth/logout');
};

