import { useApiQueryFn } from "@/shared/hooks/useApi";
import { getSaleById } from "../services/salesService";

export const useGetSaleById = (id) => {
  const { data, isLoading, error } = useApiQueryFn(['sale', id], () => getSaleById(id));
  return { data, isLoading, error };
}