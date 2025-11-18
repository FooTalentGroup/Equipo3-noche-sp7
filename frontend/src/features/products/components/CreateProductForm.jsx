'use client';

import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { toast } from 'sonner';
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage
} from '@/shared/components/ui/form';
import { Button } from '@/shared/components/ui/button';
import { Input } from '@/shared/components/ui/input';
import { Textarea } from '@/shared/components/ui/textarea';
import { Select } from '@/shared/components/ui/select';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle
} from '@/shared/components/ui/dialog';
import { useApiPost, useApiMutation } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';
import { productSchema } from '../validators/productValidators.js';
import apiClient from '@/shared/services/apiClient.js';

const CATEGORIES = [
  'Bebidas Calientes',
  'Bebidas Frías',
  'Platos Fuertes',
  'Ensaladas',
  'Postres',
  'Aperitivos'
];

export function CreateProductForm({ onClose, product, open }) {
  const queryClient = useQueryClient();
  const isEditing = !!product;

  const form = useForm({
    resolver: zodResolver(productSchema),
    defaultValues: {
      name: '',
      description: '',
      price: undefined,
      category: ''
    }
  });

  useEffect(() => {
    if (product) {
      form.reset({
        name: product.name || '',
        description: product.description || '',
        price: product.price,
        category: product.category || ''
      });
    }
  }, [product, form]);

  const createProductMutation = useApiPost('/api/products', {
    onSuccess: () => {
      queryClient.invalidateQueries(['products']);
      toast.success('Producto creado exitosamente');
      form.reset();
      onClose?.();
    },
    onError: error => {
      toast.error('Error al crear producto', {
        description: error.message || 'Por favor, intenta de nuevo.'
      });
    }
  });

  const updateProductMutation = useApiMutation(
    async data => {
      const response = await apiClient.put(`/api/products/${product.id}`, data);
      return response.data;
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['products']);
        toast.success('Producto actualizado exitosamente');
        form.reset();
        onClose?.();
      },
      onError: error => {
        toast.error('Error al actualizar producto', {
          description: error.message || 'Por favor, intenta de nuevo.'
        });
      }
    }
  );

  const onSubmit = data => {
    if (isEditing) {
      updateProductMutation.mutate(data);
    } else {
      createProductMutation.mutate(data);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className='max-w-2xl max-h-[90vh] overflow-y-auto'>
        <DialogHeader>
          <DialogTitle className='text-2xl font-bold'>
            {isEditing ? 'Editar Producto' : 'Crear Nuevo Producto'}
          </DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className='space-y-6'>
            <FormField
              control={form.control}
              name='name'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nombre del Producto</FormLabel>
                  <FormControl>
                    <Input placeholder='Ej: Hamburguesa Clásica' {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name='description'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Descripción</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder='Describe el producto...'
                      rows={4}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className='grid grid-cols-2 gap-4'>
              <FormField
                control={form.control}
                name='price'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Precio</FormLabel>
                    <FormControl>
                      <Input
                        type='number'
                        step='0.01'
                        min='0'
                        placeholder='0.00'
                        value={field.value ?? ''}
                        onChange={e => {
                          const value = e.target.value;
                          if (value === '') {
                            field.onChange(undefined);
                          } else {
                            const numValue = parseFloat(value);
                            if (!isNaN(numValue)) {
                              field.onChange(numValue);
                            }
                          }
                        }}
                        onBlur={field.onBlur}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name='category'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Categoría</FormLabel>
                    <FormControl>
                      <Select {...field}>
                        <option value=''>Selecciona una categoría</option>
                        {CATEGORIES.map(category => (
                          <option key={category} value={category}>
                            {category}
                          </option>
                        ))}
                      </Select>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className='flex gap-4 justify-end pt-4 border-t'>
              <Button type='button' variant='outline' onClick={onClose}>
                Cancelar
              </Button>
              <Button
                type='submit'
                className='bg-orange-600 hover:bg-orange-700 text-white'
                disabled={
                  isEditing
                    ? updateProductMutation.isPending
                    : createProductMutation.isPending
                }
              >
                {isEditing
                  ? updateProductMutation.isPending
                    ? 'Actualizando...'
                    : 'Actualizar Producto'
                  : createProductMutation.isPending
                  ? 'Creando...'
                  : 'Crear Producto'}
              </Button>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
