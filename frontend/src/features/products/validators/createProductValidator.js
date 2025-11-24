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
        image_file: z
            .instanceof(File, {
                message: "Por favor, selecciona una imagen.",
            })
            .refine((file) => file.type.startsWith("image/"), {
                message: "El archivo debe ser una imagen",
            })
            .refine(
                (file) =>
                    [
                        "image/jpeg",
                        "image/jpg",
                        "image/png",
                        "image/webp",
                    ].includes(file.type),
                {
                    message: "Solo se permiten imágenes JPG, PNG o WebP",
                }
            )
            .refine((file) => file.size <= 5 * 1024 * 1024, {
                message: "La imagen debe pesar menos de 5MB",
            }),
        category: z.coerce
            .number()
            .int()
            .min(1, { message: "Por favor, selecciona una categoría." })
            .max(999),

        price: z.coerce
            .number()
            .min(0.01, { message: "El precio debe ser mayor a 0." })
            .max(99999999.99),

        min_stock: z.coerce
            .number()
            .int({ message: "El stock debe ser un número entero." })
            .min(1, { message: "El stock mínimo debe ser al menos 1." })
            .max(99, { message: "El stock mínimo no puede superar los 99." }),
    })
    .strict();

export { createProductSchema };
