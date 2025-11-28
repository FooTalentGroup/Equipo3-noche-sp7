import apiClient from "@/shared/services/apiClient.js";

// Helper to safely extract data (handles axios response structure)
const unwrap = (response) => response?.data?.data ?? response;

export const getClients = async (page = 0, size = 20, sort = "") => {
  console.log("getClients function called");
  try {
    const params = { page, size };
    if (sort) params.sort = sort;

    const response = await apiClient.get("/api/clients", { params });
    const data = unwrap(response);

    console.log("Full API response:", data);

    // Spring Page response → the actual array is in "content"
    const clients = Array.isArray(data.content) ? data.content : [];

    console.log("Extracted clients:", clients);

    // Optionally return more info (useful for pagination UI)
    return {
      clients,
      totalElements: data.totalElements || 0,
      totalPages: data.totalPages || 0,
      currentPage: data.number || 0,
      pageSize: data.size || size,
      isLast: data.last || false,
    };
  } catch (error) {
    console.error("Error in getClients:", error);
    throw error;
  }
};

export const getClientById = async (id) => {
  try {
    const response = await apiClient.get(`/api/clients/${id}`);
    const data = unwrap(response);

    // For single object, backend might return { data: client } or just the client
    return data?.data ?? data ?? null;
  } catch (error) {
    console.error("Error fetching client by ID:", error);
    throw error;
  }
};

export const createClient = async (clientData) => {
  try {
    console.log("Creating client with data:", clientData);
    const response = await apiClient.post("/api/clientes", clientData); // Note: endpoint is /clientes (plural + es)
    const data = unwrap(response);

    return data?.data ?? data ?? null;
  } catch (error) {
    console.error("Error creating client:", error);
    throw error;
  }
};
