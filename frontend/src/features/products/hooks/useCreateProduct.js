import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useQueryClient } from "@tanstack/react-query";
import { createProductSchema } from "../validators/createProductValidator";
import { toast } from "sonner";
import { useApiPost } from "@/shared/hooks/useApi";
import { useState } from "react";
import { useParams } from "react-router";
import { useProducts } from "../context/ProductsContext";
import { createProduct, updateProduct as updateProductService } from "../services/productService";
import { useProduct } from "./useProduct";

const initialProductValue = {
  name: "",
  categoryId: "",
  imageFile: null,
  minStock: 0,
  price: 0,
  photoUrl: "",
};

export function useCreateProduct() {
  const [isSuccess, setIsSuccess] = useState(false);
  const { addProduct, updateProduct } = useProducts();
  const { updateProductLocal } = useProduct()
  const { id } = useParams();
  const isEditing = !!id;

  // eslint-disable-next-line no-unused-vars
  const queryClient = useQueryClient();
  const form = useForm({
    resolver: zodResolver(createProductSchema),
    defaultValues: initialProductValue,
  });

  const createMutation = useApiPost("/api/products", {
    mutationFn: (data) => {
      const dataToSend = {
        ...data,
        imageFile: undefined
      };
      return createProduct(dataToSend);
    },
    onSuccess: (newProduct) => {
      addProduct(newProduct.data);
      // queryClient.invalidateQueries(["products"]);
      toast.success("Producto creado exitosamente");
      setIsSuccess(true);
      form.reset();
    },
    onError: (error) => {
      toast.error("Error al crear producto", {
        description: error.message || "Por favor, intenta de nuevo.",
      });
    },
  });

  const updateMutation = useApiPost(`/api/products/${id}`, {
    mutationFn: (data) => {
      const dataToSend = {
        ...data,
        imageFile: undefined
      };
      return updateProductService(id, dataToSend);
    },
    onSuccess: (updatedProduct) => {
      updateProduct(id, updatedProduct.data);
      updateProductLocal(updatedProduct.data);
      // queryClient.invalidateQueries(["products"]);
      toast.success("Producto actualizado exitosamente");
      setIsSuccess(true);
      form.reset();
    },
    onError: (error) => {
      toast.error("Error al actualizar producto", {
        description: error.message || "Por favor, intenta de nuevo.",
      });
    },
  });

  const handlePost = (data) => {
    if (isEditing) {
      updateMutation.mutate(data);
    } else {
      createMutation.mutate(data);
    }
  };

  const handleError = (errors) => {
    console.log("Errores de validación:", errors);
    toast.error("Por favor, corrige los errores en el formulario");
  };

  const resetSuccess = () => {
    setIsSuccess(false);
  };

  const isPending = createMutation.isPending || updateMutation.isPending;
  const isError = createMutation.isError || updateMutation.isError;

  return { 
    form, 
    handlePost, 
    handleError, 
    isPending, 
    isSuccess, 
    isError,
    resetSuccess 
  };
}