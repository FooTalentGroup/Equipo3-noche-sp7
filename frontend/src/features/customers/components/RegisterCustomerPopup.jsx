// src/features/customers/components/RegisterCustomerPopup.jsx
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Button } from '@/shared/components/ui/button';
import { Input } from '@/shared/components/ui/input';
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/shared/components/ui/form.jsx';

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function RegisterCustomerPopup({ open, onClose, onSave, initialData = null }) {
    const isEditMode = !!initialData?.id;

    const form = useForm({
        defaultValues: {
            nombre: '',
            telefono: '',
            email: '',
            joined: true,
        },
    });

    // Reset form when popup opens or initialData changes
    useEffect(() => {
        if (open) {
            form.reset({
                nombre: initialData?.nombre ?? '',
                telefono: initialData?.telefono ?? '',
                email: initialData?.email ?? '',
                joined: initialData?.joined ?? true,
            });
        }
    }, [initialData, open, form]);

    if (!open) return null;

    const onSubmit = (values) => {
        onSave?.({
            id: initialData?.id,
            nombre: values.nombre,
            telefono: values.telefono,
            email: values.email,
            joined: values.joined,
        });
    };

    const buildRequiredMessage = (label) => `${label} es obligatorio`;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
            <div className="absolute inset-0 bg-black/40" onClick={onClose} />
            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className="relative w-[700px] max-w-full bg-[#F4F5F7] rounded-xl shadow-lg p-10 z-10 flex flex-col min-h-[500px] animate-in fade-in-50 zoom-in-95"
                >
                    <h3 className="text-lg font-semibold">
                        {isEditMode ? 'Editar cliente' : 'Registrar cliente'}
                    </h3>
                    <p className="text-xs text-gray-500 mt-1">
                        {isEditMode
                            ? 'Actualiza la información del cliente'
                            : 'Completa este formulario para registrar un nuevo cliente'}
                    </p>
                    <hr className="my-6 border-gray-300" />

                    <div className="space-y-5 flex-1">
                        <FormField
                            control={form.control}
                            name="nombre"
                            rules={{
                                required: !isEditMode && buildRequiredMessage('Nombre'),
                                minLength: { value: 3, message: 'El nombre debe tener al menos 3 caracteres' },
                            }}
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-sm font-medium text-gray-700 flex items-center gap-1">
                                        Nombre {!isEditMode && <span className="text-red-500">*</span>}
                                    </FormLabel>
                                    <FormControl>
                                        <Input className="mt-1 bg-white shadow-sm" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        <FormField
                            control={form.control}
                            name="telefono"
                            rules={{
                                required: !isEditMode && buildRequiredMessage('Número telefónico'),
                                minLength: { value: 6, message: 'Número telefónico inválido' },
                            }}
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-sm font-medium text-gray-700 flex items-center gap-1">
                                        Número telefónico {!isEditMode && <span className="text-red-500">*</span>}
                                    </FormLabel>
                                    <FormControl>
                                        <Input className="mt-1 bg-white shadow-sm" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        <FormField
                            control={form.control}
                            name="email"
                            rules={{
                                required: !isEditMode && buildRequiredMessage('Correo electrónico'),
                                pattern: { value: emailRegex, message: 'Correo electrónico inválido' },
                            }}
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-sm font-medium text-gray-700 flex items-center gap-1">
                                        Correo electrónico {!isEditMode && <span className="text-red-500">*</span>}
                                    </FormLabel>
                                    <FormControl>
                                        <Input type="email" className="mt-1 bg-white shadow-sm" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>

                    <div className="flex justify-between items-start mt-8 pt-6">
                        <div className="flex-1 pr-8">
                            <div className="text-sm font-medium text-gray-700">Unirse a la comunidad Pro-eat</div>
                            <p className="text-xs text-gray-500 mt-1">
                                El cliente recibirá promociones y descuentos en su correo electrónico
                            </p>
                        </div>

                        <FormField
                            control={form.control}
                            name="joined"
                            render={({ field }) => (
                                <label className="relative inline-flex items-center cursor-pointer">
                                    <input
                                        type="checkbox"
                                        className="sr-only peer"
                                        checked={field.value}
                                        onChange={field.onChange}
                                    />
                                    <div className="w-[33px] h-[18px] bg-gray-200 peer-checked:bg-[#545F66] rounded-full transition-colors duration-300" />
                                    <div className="pointer-events-none absolute inset-0 flex items-center px-1">
                                        <div
                                            className={`h-4 w-4 bg-white rounded-full shadow-lg transform transition-transform duration-300 ease-in-out ${field.value ? 'translate-x-3' : 'translate-x-0'
                                                }`}
                                        />
                                    </div>
                                </label>
                            )}
                        />
                    </div>

                    <div className="mt-10 flex justify-start gap-6 pt-6">
                        <Button
                            type="button"
                            variant="outline"
                            className="w-36 h-11 text-[#404040] border-gray-300 hover:bg-gray-200"
                            onClick={onClose}
                        >
                            Cancelar
                        </Button>
                        <Button type="submit" className="w-44 h-11 bg-[#436086] hover:bg-[#364d6e] text-white font-medium">
                            {isEditMode ? 'Guardar cambios' : 'Guardar cliente'}
                        </Button>
                    </div>
                </form>
            </Form>
        </div>
    );
}