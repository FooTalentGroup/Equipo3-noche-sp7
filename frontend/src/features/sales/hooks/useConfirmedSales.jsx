import { getConfirmedSales } from "../services/salesService";
import { useApiQueryFn } from "@/shared/hooks/useApi";

export const useConfirmedSales = () => {
  const { data, isLoading, error } = useApiQueryFn(['confirmed-sales'], getConfirmedSales);
  return { data, isLoading, error };
};