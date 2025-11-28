import { useEffect, useMemo, useRef, useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from '@/shared/components/ui/dialog.jsx';
import { Button } from '@/shared/components/ui/button.jsx';
import { Input } from '@/shared/components/ui/input.jsx';
import { Textarea } from '@/shared/components/ui/textarea.jsx';
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/shared/components/ui/form.jsx';
import { ArrowDownCircle, ArrowRightLeft, ArrowUpCircle, Building2, ClipboardList, Package } from 'lucide-react';
import { cn } from '@/lib/utils';
import { createInventoryMovement } from '@/features/products/services/inventoryService.js';
import { useApiMutation } from '@/shared/hooks/useApi.js';
import { toast } from 'sonner';

const adjustmentSchema = z.object({
    quantity: z.coerce.number({ required_error: 'La cantidad es obligatoria' })
        .int('La cantidad debe ser un número entero')
        .min(1, 'La cantidad debe ser mayor a 0'),
    supplier: z.string().max(100, 'Máximo 100 caracteres').optional().or(z.literal('')),
    cost: z.coerce.number({ required_error: 'El costo es obligatorio' })
        .min(0, 'El costo debe ser mayor o igual a 0'),
    observations: z.string().max(500, 'Máximo 500 caracteres').optional().or(z.literal('')),
});

export function ProductAdjustmentDialog({ open, onOpenChange, product }) {
    const [activeTab, setActiveTab] = useState('entrada');
    const productRef = useRef(product);
    const form = useForm({
        resolver: zodResolver(adjustmentSchema),
        defaultValues: {
            quantity: '',
            supplier: '',
            cost: '',
            observations: '',
        },
    });

    useEffect(() => {
        productRef.current = product;
    }, [product]);

    useEffect(() => {
        if (!open) return;
        const supplierValue = productRef.current?.supplierName
            ?? productRef.current?.proveedor
            ?? '';

        setActiveTab('entrada');
        form.reset({
            quantity: '',
            supplier: supplierValue,
            cost: '',
            observations: '',
        });
    }, [open, form]);

    const movementMutation = useApiMutation(createInventoryMovement, {
        onSuccess: () => {
            toast.success('Movimiento registrado correctamente');
            handleClose();
        },
        onError: (error) => {
            console.error('Error creating movement', error);
            toast.error('No se pudo registrar el movimiento. Intenta nuevamente.');
        },
    });

    const currentStock = Number(product?.currentStock ?? product?.stock_actual ?? 0);
    const quantityValue = Number(form.watch('quantity') || 0);
    const resultingStock = useMemo(() => {
        return activeTab === 'entrada'
            ? currentStock + quantityValue
            : Math.max(0, currentStock - quantityValue);
    }, [activeTab, currentStock, quantityValue]);

    const handleClose = () => {
        if (movementMutation.isPending) return;
        onOpenChange?.(false);
    };

    const onSubmit = (values) => {
        const targetProduct = productRef.current;

        if (!targetProduct?.id) {
            toast.error('No se encontró información del producto.');
            return;
        }

        movementMutation.mutate({
            productId: targetProduct.id,
            movementType: activeTab === 'entrada' ? 'IN' : 'OUT',
            quantity: Number(values.quantity),
            reason: values.observations?.trim() || '',
            purchaseCost: Number(values.cost),
        });
    };

    const categoryLabel = product?.category?.name ?? product?.categoryName ?? product?.category ?? 'Sin categoría';
    const supplierLabel = product?.supplierName ?? product?.proveedor ?? 'Proveedor no registrado';

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="max-w-3xl p-0 overflow-hidden">
                <DialogHeader className="px-8 pt-8">
                    <DialogTitle className="text-2xl font-semibold text-gray-900">
                        Ajuste de Inventario
                    </DialogTitle>
                    <DialogDescription className="text-base text-gray-700">
                        {product?.name || 'Producto sin nombre'}
                    </DialogDescription>
                </DialogHeader>

                <div className="px-8 pb-8">
                    <div className="rounded-2xl border border-slate-200 bg-slate-50 p-6 mb-8">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-slate-500 flex items-center gap-2">
                                    <Package className="h-4 w-4 text-slate-600" />
                                    Stock actual en almacén
                                </p>
                                <p className="text-4xl font-semibold text-slate-900 mt-2">
                                    {currentStock}
                                    <span className="text-base font-medium text-slate-500 ml-2">unid.</span>
                                </p>
                            </div>
                            <div className="flex flex-col gap-2 text-sm text-slate-600">
                                <span className="flex items-center gap-2">
                                    <Building2 className="h-4 w-4 text-slate-700" />
                                    {categoryLabel}
                                </span>
                                <span className="flex items-center gap-2">
                                    <ClipboardList className="h-4 w-4 text-slate-700" />
                                    {supplierLabel}
                                </span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-slate-100 rounded-xl p-1 mb-6 flex gap-2">
                        <button
                            type="button"
                            className={cn(
                                'flex-1 flex items-center justify-center gap-2 px-4 py-3 rounded-lg text-sm font-medium transition',
                                activeTab === 'entrada'
                                    ? 'bg-slate-900 text-white shadow'
                                    : 'text-slate-600 hover:text-slate-900'
                            )}
                            onClick={() => setActiveTab('entrada')}
                        >
                            <ArrowDownCircle className="h-4 w-4" />
                            Entrada (Compra)
                        </button>
                        <button
                            type="button"
                            className={cn(
                                'flex-1 flex items-center justify-center gap-2 px-4 py-3 rounded-lg text-sm font-medium transition',
                                activeTab === 'salida'
                                    ? 'bg-slate-900 text-white shadow'
                                    : 'text-slate-600 hover:text-slate-900'
                            )}
                            onClick={() => setActiveTab('salida')}
                        >
                            <ArrowUpCircle className="h-4 w-4" />
                            Salida (Venta/Baja)
                        </button>
                    </div>

                    <Form {...form}>
                        <form className="space-y-6" onSubmit={form.handleSubmit(onSubmit)}>
                            <div className="grid grid-cols-1 md:grid-cols-[minmax(0,1fr)_auto] gap-6 items-end">
                                <FormField
                                    control={form.control}
                                    name="quantity"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-gray-700">
                                                Cantidad a procesar <span className="text-red-500">*</span>
                                            </FormLabel>
                                            <FormControl>
                                                <Input
                                                    type="number"
                                                    min={1}
                                                    placeholder="Ingresar cantidad"
                                                    className="mt-2 bg-white"
                                                    {...field}
                                                />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                                <div className="flex items-center gap-2 text-gray-700">
                                    <ArrowRightLeft className="h-4 w-4 text-slate-600" />
                                    Stock resultante:
                                    <span className="text-lg font-semibold text-slate-900">
                                        {resultingStock}
                                    </span>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <FormField
                                    control={form.control}
                                    name="supplier"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-gray-700">
                                                Proveedor <span className="text-slate-500">(opcional)</span>
                                            </FormLabel>
                                            <FormControl>
                                                <Input
                                                    placeholder="Colocar nombre del proveedor"
                                                    className="mt-2 bg-white"
                                                    {...field}
                                                />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="cost"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-gray-700">
                                                Costo de compra <span className="text-red-500">*</span>
                                            </FormLabel>
                                            <FormControl>
                                                <Input
                                                    type="number"
                                                    min={0}
                                                    step="0.01"
                                                    placeholder="Colocar costo de compra"
                                                    className="mt-2 bg-white"
                                                    {...field}
                                                />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                            </div>

                            <FormField
                                control={form.control}
                                name="observations"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel className="text-gray-700">
                                            Motivo / Observaciones
                                        </FormLabel>
                                        <FormControl>
                                            <Textarea
                                                rows={4}
                                                placeholder="Agregar motivo / observaciones"
                                                className="mt-2 bg-white resize-none"
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />

                            <div className="flex flex-col md:flex-row justify-end gap-3 pt-4">
                                <Button type="button" variant="outline" onClick={handleClose} disabled={movementMutation.isPending}>
                                    Cancelar
                                </Button>
                                <Button type="submit" className="bg-slate-800 hover:bg-slate-900" disabled={movementMutation.isPending}>
                                    {movementMutation.isPending ? 'Guardando...' : 'Guardar movimiento'}
                                </Button>
                            </div>
                        </form>
                    </Form>
                </div>
            </DialogContent>
        </Dialog>
    );
}


