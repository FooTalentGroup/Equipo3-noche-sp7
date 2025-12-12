import { getPendingSales } from "../services/salesService";
import { useApiQueryFn } from "@/shared/hooks/useApi";

export const usePendingSales = () => {
  const { data, isLoading, error } = useApiQueryFn(['pending-sales'], () => getPendingSales());
  return { data, isLoading, error };
};