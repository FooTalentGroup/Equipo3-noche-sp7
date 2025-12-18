import apiClient from "@/shared/services/apiClient";

export async function getSalesLastWeek() {
  const endDate = new Date();
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - 7);

  const format = (d) => d.toISOString().split("T")[0];

  const params = new URLSearchParams({
    startDate: format(startDate),
    endDate: format(endDate),
  });

  const { data } = await apiClient.get(`/api/orders?${params.toString()}`);
  return data;
}

export async function getPendingSales() {
  const { data } = await apiClient.get('/api/orders?status=PENDING');
  return data;
}

export async function getConfirmedSales() {
  const { data } = await apiClient.get('/api/orders?status=CONFIRMED');
  return data;
}

export async function getCancelledSales() {
  const { data } = await apiClient.get('/api/orders?status=CANCELLED');
  return data;
}

export async function createSale(orderData) {
  const { data } = await apiClient.post('/api/orders', orderData);
  return data;
}

export async function getSaleById(id) {
  const { data } = await apiClient.get(`/api/orders/${id}`);
  return data;
}

export async function confirmSale(id) {
  const { data } = await apiClient.patch(`/api/orders/${id}/confirm`);
  return data;
}

export async function cancelOrder(id) {
  const { data } = await apiClient.patch(`/api/orders/${id}/cancel`, {
    cancelReason: "Cancelado por el cliente"
  });
  return data;
}

export async function getSaleTicket(id) {
  try {
    const response = await apiClient.get(`/api/orders/${id}/pdf`, {
      responseType: 'blob',
    });
    return response.data;

  } catch (error) {
    if (error.response && error.response.data instanceof Blob) {
    }
    throw error;
  }
}
