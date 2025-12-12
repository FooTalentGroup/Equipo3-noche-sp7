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

export async function getConfirmedOrdersToday() {
  const { data } = await apiClient.get('/api/orders?status=CONFIRMED');
  return data;
}

export async function getCancelledOrders() {
  const { data } = await apiClient.get('/api/orders?status=CANCELLED');
  return data;
}

export async function createSale(orderData) {
  const { data } = await apiClient.post('/api/orders', orderData);
  return data;
}