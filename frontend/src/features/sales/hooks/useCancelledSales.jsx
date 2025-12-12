import { getCancelledSales } from "../services/salesService";
import { useApiQueryFn } from "@/shared/hooks/useApi";

export const useCancelledSales = () => {
  const { data, isLoading, error } = useApiQueryFn(['sales', 'cancelled'], getCancelledSales, {
    invalidatQueries: ['sales', 'pending', 'confirmed', 'cancelled'],
  });
  return { data, isLoading, error };
};
