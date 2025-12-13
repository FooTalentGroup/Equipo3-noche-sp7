import { useQueryClient } from "@tanstack/react-query";
import { useApiMutation } from "@/shared/hooks/useApi";
import { createSale } from "../services/salesService";

export const useOrderManagement = () => {
  const queryClient = useQueryClient();

  const createOrderMutation = useApiMutation(createSale, {
    onSuccess: () => {
      queryClient.invalidateQueries(["sales", "cancelled"]);
    }
  });

  const handleCreateOrder = async (orderData) => {
    await createOrderMutation.mutateAsync(orderData);
  };



  return {
    handleCreateOrder,
    isPending: createOrderMutation.isPending
  };
};