import { z } from 'zod';

export const productSchema = z.object({
  name: z.string().min(1, 'El nombre es requerido').max(100, 'El nombre es muy largo'),
  description: z.string().min(1, 'La descripción es requerida').max(500, 'La descripción es muy larga'),
  price: z
    .number({
      required_error: 'El precio es requerido',
      invalid_type_error: 'El precio debe ser un número'
    })
    .min(0.01, 'El precio debe ser mayor a 0'),
  category: z.string().min(1, 'La categoría es requerida'),
});

