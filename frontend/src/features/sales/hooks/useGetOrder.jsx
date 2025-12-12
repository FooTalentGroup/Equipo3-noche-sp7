import { useApiQueryFn } from "@/shared/hooks/useApi";
import { getSaleById } from "../services/salesService";

export const useGetOrder = (orderId, options = {}) => {
  return useApiQueryFn(
    ["order", orderId],
    () => getSaleById(orderId),
    {
      enabled: !!orderId,
      ...options,
    }
  );
};
