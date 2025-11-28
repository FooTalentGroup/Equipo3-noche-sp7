// src/features/customers/services/customerService.js
import apiClient from "@/shared/services/apiClient.js";

// Safely extract data from axios response
const unwrap = (response) => response?.data?.data ?? response;

export const getCustomers = async ({
  page = 0,
  size = 10,
  search = "",
  sort = "",
} = {}) => {
  const params = { page, size };
  if (search) params.search = search;
  if (sort) params.sort = sort;

  try {
    console.log("[customerService] Fetching customers:", params);
    const response = await apiClient.get("/api/clients", { params });
    const data = unwrap(response);

    const content = Array.isArray(data?.content) ? data.content : [];

    return {
      customers: content,
      totalElements: data?.totalElements ?? content.length,
      totalPages: data?.totalPages ?? 1,
      currentPage: data?.number ?? page,
      pageSize: data?.size ?? size,
    };
  } catch (error) {
    console.error("Error fetching customers:", error);
    throw error;
  }
};

export const createCustomer = async (customerData) => {
  try {
    const response = await apiClient.post("/api/clients", customerData); // Fixed: use /clients
    return unwrap(response);
  } catch (error) {
    console.error("Error creating customer:", error);
    throw error;
  }
};

export const updateCustomer = async (id, customerData) => {
  try {
    const response = await apiClient.put(`/api/clients/${id}`, customerData); // Fixed: use /clients
    return unwrap(response);
  } catch (error) {
    console.error("Error updating customer:", error);
    throw error;
  }
};
