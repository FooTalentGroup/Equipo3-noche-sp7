import apiClient from '@/shared/services/apiClient.js';

export const createInventoryMovement = async (movementData) => {
  const { data } = await apiClient.post('/api/inventory/movements', movementData);
  return data;
};

