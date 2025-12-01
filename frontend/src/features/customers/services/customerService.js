// src/features/customers/services/customerService.js
import apiClient from "@/shared/services/apiClient.js";

// Safely extract data from axios response
const unwrap = (res) => res?.data?.data?? res;

export async function getCustomers({ page = 0, size = 10, name = "" } = {}) {
  const params = { page, size };
  if (name) params.name = name;
  const res = await apiClient.get("/api/clients", { params });
  console.log(res);
  
  const data = unwrap(res);

  console.log(data);
  return {
    customers: data?.content ?? data?.customers ?? Array.isArray(data) ? data : [],
    totalPages: data?.totalPages ?? data?.page?.totalPages ?? 1,
    totalElements: data?.totalElements ?? data?.page?.totalElements ?? (data?.content?.length ?? 0),
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
