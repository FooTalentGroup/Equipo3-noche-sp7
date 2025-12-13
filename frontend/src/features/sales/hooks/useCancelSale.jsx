import { useApiMutation } from "@/shared/hooks/useApi";
import { cancelOrder } from "../services/salesService";
import { queryClient } from "@/lib/query-client";

export const useCancelSale = () => {
  const { mutateAsync, isLoading, isError, error } = useApiMutation(cancelOrder, {
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["pending-sales"],
      });
    },
  });
  return { mutateAsync, isLoading, isError, error };
};