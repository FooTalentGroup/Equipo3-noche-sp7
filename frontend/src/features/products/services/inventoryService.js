import apiClient from '@/shared/services/apiClient.js';

const unwrap = (res) => res?.data?.data ?? res?.data ?? res;

export async function createInventoryMovement(movementData) {
  try {
    const response = await apiClient.post('/api/inventory-movements', movementData);
    return unwrap(response);
  } catch (error) {
    console.error('Error creating inventory movement:', error);
    throw error;
  }
}

export async function getInventoryMovements({ page = 0, size = 10, productId = '', productName = '', userName = '', movementType = '', startDate = '', endDate = '' } = {}) {
  try {
    const params = { page, size };
    if (productId) params.productId = productId;
    if (productName) params.productName = productName;
    if (userName) params.userName = userName;
    if (movementType && movementType !== 'all') params.movementType = movementType;
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;

    const response = await apiClient.get('/api/inventory-movements', { params });
    console.log("Inventory response:\n", response);
    
    const data = unwrap(response);
    console.log("Unwrapped data:\n", data);
    
    // Handle both paginated and array responses
    const movements = data?.content ?? (Array.isArray(data) ? data : []);
    
    return {
      movements: movements,
      totalPages: data?.totalPages ?? 1,
      totalElements: data?.totalElements ?? movements.length,
      pageSize: size,
    };
  } catch (error) {
    console.error('Error fetching inventory movements:', error);
    throw error;
  }
}

export async function getInventoryMovementsByProduct(productId) {
  try {
    const response = await apiClient.get('/api/inventory-movements', { 
      params: { productId } 
    });
    return unwrap(response);
  } catch (error) {
    console.error('Error fetching inventory movements by product:', error);
    throw error;
  }
}

export async function getInventoryMovementById(id) {
  try {
    const response = await apiClient.get(`/api/inventory-movements/${id}`);
    return unwrap(response);
  } catch (error) {
    console.error('Error fetching inventory movement:', error);
    throw error;
  }
}

