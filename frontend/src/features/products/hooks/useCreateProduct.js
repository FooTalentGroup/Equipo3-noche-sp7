import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useQueryClient } from "@tanstack/react-query";
import { createProductSchema } from "../validators/createProductValidator";
import { toast } from "sonner";
// eslint-disable-next-line no-unused-vars
import { useApiPost } from "@/shared/hooks/useApi";
import { useState } from "react";

const initialProductValue = {
  name: "",
  category: 0,
  image_file: null,
  min_stock: 0,
  cost_price: 0,
  price: 0,
  provider: 0,
};

export function useCreateProduct() {
  const [isPending, setIsPending] = useState(false);
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
      toast.success("Producto creado exitosamente");
      console.log(data);
      // form.reset();
    } finally {
      setIsPending(false);
    }
  };

  const handleError = (errors) => {
    console.log("Errores de validación:", errors);
    toast.error("Por favor, corrige los errores en el formulario");
  };
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
  return { form, handlePost, handleError, isPending };
}
