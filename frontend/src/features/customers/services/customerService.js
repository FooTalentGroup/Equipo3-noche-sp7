import apiClient from "@/shared/services/apiClient.js";

const unwrap = (response) => response?.data ?? response;

export const getClients = async (params = {}) => {
  const {
    page = 0,
    size = 20,
    sort,
    name,
    email,
    phone,
    isFrequent,
  } = params;

  const queryParams = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  if (sort) {
    queryParams.append('sort', sort);
  }

  if (name) {
    queryParams.append('name', name);
  }

  if (email) {
    queryParams.append('email', email);
  }

  if (phone) {
    queryParams.append('phone', phone);
  }

  if (isFrequent !== undefined && isFrequent !== null) {
    queryParams.append('isFrequent', isFrequent.toString());
  }

  const { data } = await apiClient.get(`/api/clients?${queryParams.toString()}`);
  const payload = unwrap(data);
  
  // Return the full paginated response
  if (payload?.content) {
    return payload;
  }
  
  // Fallback for non-paginated responses
  return {
    content: Array.isArray(payload?.data)
      ? payload.data
      : Array.isArray(payload)
        ? payload
        : [],
    totalElements: Array.isArray(payload?.data) ? payload.data.length : Array.isArray(payload) ? payload.length : 0,
    totalPages: 1,
    number: 0,
    size: size,
  };
};

export const getClientById = async (id) => {
  const { data } = await apiClient.get(`/api/clients/${id}`);
  const payload = unwrap(data);
  return payload?.data ?? payload ?? null;
};

export const createClient = async (clientData) => {
  console.log(clientData);

  const { data } = await apiClient.post("/api/clientes", clientData);
  const payload = unwrap(data);
  return payload?.data ?? payload ?? null;
};
