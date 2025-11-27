import apiClient from "@/shared/services/apiClient";

export const getCategories = async () => {
  const { data } = await apiClient.get(`/api/categories`);
  return data;
};