import { useApiMutation } from "@/shared/hooks/useApi";
import { confirmSale } from "../services/salesService";
import { queryClient } from "@/lib/query-client";

export const useConfirmSale = (id) => {
  const { mutateAsync, isPending, isSuccess, isError, error, status, reset } = useApiMutation(() => confirmSale(id), {
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["pending-sales"]
      });
    },
    onError: (error) => {
    }
  });

  return { mutateAsync, isPending, isSuccess, isError, error, status, reset };
}