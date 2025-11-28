import apiClient from '@/shared/services/apiClient.js';

export const createInventoryMovement = async (payload) => {
    const { data } = await apiClient.post('/inventory/movements', payload);
    return data;
};



