import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useQueryClient } from "@tanstack/react-query";
import { createProductSchema } from "../validators/createProductValidator";
import { toast } from "sonner";
// eslint-disable-next-line no-unused-vars
import { useApiPost } from "@/shared/hooks/useApi";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router";

const initialProductValue = {
  name: "",
  category: 0,
  image_file: null,
  min_stock: 0,
  price: 0,
};

export function useCreateProduct() {
  const [isPending, setIsPending] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const navigate = useNavigate();
  // eslint-disable-next-line no-unused-vars
  const queryClient = useQueryClient();
  const form = useForm({
    resolver: zodResolver(createProductSchema),
    defaultValues: initialProductValue,
    mode: "onSubmit"
  });

  const handlePost = async (data) => {
    console.log("handlePost ejecutado con datos:", data);
    setIsPending(true);
    try {
      await new Promise((resolve) => setTimeout(resolve, 1500));
      setIsPending(false);
      setIsSuccess(true);
      console.log(data);
      // form.reset();
    } catch (error) {
      setIsPending(false);
      throw error;
    }
  };

  const handleError = (errors) => {
    console.log("Errores de validación:", errors);
    toast.error("Por favor, corrige los errores en el formulario");
  };

  const resetSuccess = () => {
    setIsSuccess(false);
  };

  useEffect(() => {
    if (isSuccess) {
      const timer = setTimeout(() => {
        navigate("/products");
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [isSuccess, navigate]);

  // const handlePost = useApiPost("/api/products", {
  //   onSuccess: () => {
  //     // queryClient.invalidateQueries(["products"]);
  //     toast.success("Producto creado exitosamente");
  //     form.reset();
  //     onClose?.();
  //   },
  //   onError: (error) => {
  //     toast.error("Error al crear producto", {
  //       description: error.message || "Por favor, intenta de nuevo.",
  //     });
  //   },
  // });
  return { form, handlePost, handleError, isPending, isSuccess, resetSuccess };
}