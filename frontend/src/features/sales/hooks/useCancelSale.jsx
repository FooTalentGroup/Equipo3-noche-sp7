import { useApiMutation } from "@/shared/hooks/useApi";
import { cancelOrder } from "../services/salesService";

export const useCancelSale = () => {
  const { mutateAsync, isLoading, isError, error } = useApiMutation(cancelOrder);
  return { mutateAsync, isLoading, isError, error };
};