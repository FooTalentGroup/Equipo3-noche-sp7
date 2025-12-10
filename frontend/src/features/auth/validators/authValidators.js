import { z } from 'zod';

export const loginSchema = z.object({
  email: z
    .string()
    .min(1, 'El correo es obligatorio')
    .email('Debe ingresar un correo electrónico válido'),
  password: z
    .string()
    .min(1, 'La contraseña es obligatoria')
    .min(6, 'La contraseña debe tener al menos 6 caracteres'),
});

export const registerSchema = z.object({
  email: z
    .string()
    .min(1, 'El correo es obligatorio')
    .email('Debe ingresar un correo electrónico válido'),
  password: z
    .string()
    .min(1, 'La contraseña es obligatoria')
    .min(6, 'La contraseña debe tener al menos 6 caracteres')
    .max(50, 'La contraseña no puede tener más de 50 caracteres'),
});

export const forgotPasswordSchema = z.object({
  email: z
    .string()
    .min(1, "El correo electrónico es requerido")
    .email("Ingresa un correo electrónico válido"),
});

export const resetPasswordSchema = z.object({
    password: z
      .string()
      .min(1, "La contraseña es requerida")
      .min(8, "Mínimo 8 caracteres, incluyendo 1 mayúscula, 1 minúscula y 1 número"),
    confirmPassword: z.string().min(1, "Debes repetir la contraseña"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Las contraseñas no coinciden",
    path: ["confirmPassword"],
  });