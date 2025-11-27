import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { deleteProduct } from "../services/productService";
import { useProducts } from "../context/ProductsContext";

export function useDeleteProduct() {
  const queryClient = useQueryClient();
  const { removeProduct } = useProducts();

  const deleteMutation = useMutation({
    mutationFn: (productId) => deleteProduct(productId),
    onSuccess: (_, productId) => {
      removeProduct(productId);
      queryClient.invalidateQueries({ queryKey: ["products"] });
      toast.success("Producto eliminado exitosamente");
    },
    onError: (error) => {
      toast.error("Error al eliminar producto", {
        description: error.message || "Por favor, intenta de nuevo.",
      });
    },
  });

  const handleDelete = (productId) => {
    deleteMutation.mutate(productId);
  };

  return {
    handleDelete,
    isPending: deleteMutation.isPending,
    isSuccess: deleteMutation.isSuccess,
  };
}