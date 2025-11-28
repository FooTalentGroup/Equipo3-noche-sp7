// hooks/useProduct.js
import { useParams } from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";
import { useApiQueryFn } from "@/shared/hooks/useApi";
import { getProductById } from "../services/productService";
import { useProducts } from "../context/ProductsContext";

export function useProduct() {
  const { id } = useParams();
  const { products } = useProducts();
  const queryClient = useQueryClient();
  
  const cachedProduct = products.find(p => p.id === parseInt(id));

  const { data, isLoading, error } = useApiQueryFn(
    ['product', id],
    () => getProductById(id),
    {
      initialData: cachedProduct,
      staleTime: 0,
      enabled: !!id,
    }
  );

  const updateProductLocal = (updatedData) => {
    queryClient.setQueryData(['product', id], (oldData) => ({
      ...oldData,
      ...updatedData,
    }));
  };

  const refetchProduct = () => {
    queryClient.invalidateQueries(['product', id]);
  };

  return {
    product: data,
    isLoading,
    error,
    productId: id,
    updateProductLocal,
    refetchProduct,
  };
}