import apiClient from "@/shared/services/apiClient.js";

const unwrap = (res) => res?.data?.data ?? res;

export async function getCustomers({ page = 0, size = 10, name = "" } = {}) {
  const params = { page, size };
  if (name) params.name = name;
  const res = await apiClient.get("/api/clients", { params });

  const data = unwrap(res);

  const content = data?.content ?? data?.customers ?? (Array.isArray(data) ? data : []);

  return {
    customers: content,
    totalPages: data?.totalPages ?? data?.page?.totalPages ?? 1,
    totalElements: data?.totalElements ?? data?.page?.totalElements ?? (content.length ?? 0),
    pageSize: size,
  };
}

export async function createCustomer(payload) {
  const body = {
    name: payload.name,
    email: payload.email,
    phone: payload.phone,
    isFrequent: payload.isFrequent ?? false,
  };
  const res = await apiClient.post("/api/clients", body);
  return unwrap(res);
}

export async function updateCustomer(id, payload) {
  const res = await apiClient.put(`/api/clients/${id}`, payload);
  return unwrap(res);
}

export async function getCustomerById(id) {
  const res = await apiClient.get(`/api/clients/${id}`);
  return unwrap(res);
}

export async function getPurchaseHistory(id, { page = 0, size = 10 } = {}) {
  const params = { page, size };
  const res = await apiClient.get(`/api/clients/${id}/purchase-history`, { params });

  const data = unwrap(res);

  const purchases = Array.isArray(data) ? data : (data?.purchases ?? []);

  return {
    purchases: purchases,
    totalPages: res.data?.totalPages ?? 1,
    totalElements: res.data?.totalElements ?? purchases.length,
    pageSize: size,
  };
}

export async function deleteCustomer(id) {
  const res = await apiClient.delete(`/api/clients/${id}`);
  return unwrap(res);
}
