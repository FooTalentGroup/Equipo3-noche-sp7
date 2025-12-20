import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateSale } from "../services/salesService";

export const useUpdateOrder = () => {
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: ({ orderId, orderData }) => {
      return updateSale(orderId, orderData);
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["orders"] });
      queryClient.invalidateQueries({ queryKey: ["pending-sales"] });
      queryClient.invalidateQueries({ queryKey: ["confirmed-sales"] });
      queryClient.invalidateQueries({ queryKey: ["cancelled-sales"] });
      queryClient.invalidateQueries({ queryKey: ['order', data.id] });
    },
    onError: (error) => {
      console.error("Error en updateOrder:", error);
    }
  });

  return { 
    handleUpdateOrder: mutateAsync, 
    isPending
  };
};