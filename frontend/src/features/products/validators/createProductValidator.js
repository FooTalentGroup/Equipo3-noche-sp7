import { z } from "zod";

const createProductSchema = z
  .object({
    name: z
      .string()
      .trim()
      .min(3, { message: "El nombre debe tener al menos 3 caracteres." })
      .max(100, {
        message: "El nombre no puede superar los 100 caracteres.",
      }),
    imageFile: z
      .instanceof(File)
      .refine((file) => file.type.startsWith("image/"), {
        message: "El archivo debe ser una imagen",
      })
      .refine(
        (file) =>
          ["image/jpeg", "image/jpg", "image/png", "image/webp"].includes(
            file.type
          ),
        {
          message: "Solo se permiten imágenes JPG, PNG o WebP",
        }
      )
      .refine((file) => file.size <= 5 * 1024 * 1024, {
        message: "La imagen debe pesar menos de 5MB",
      })
      .optional()
      .or(z.null()),
    photoUrl: z
      .url({ message: "Debe ser una URL válida" })
      .optional()
      .or(z.literal("")),
    categoryId: z.uuidv4({ message: 'Debe ser un UUID v4' }),
    price: z.coerce
      .number()
      .min(0.01, { message: "El precio debe ser mayor a 0." })
      .max(99999999.99),
    minStock: z.coerce
      .number()
      .int({ message: "El stock debe ser un número entero." })
      .min(1, { message: "El stock mínimo debe ser al menos 1." })
      .max(999, { message: "El stock mínimo no puede superar los 999." }),
  })
  .strict();

export { createProductSchema };