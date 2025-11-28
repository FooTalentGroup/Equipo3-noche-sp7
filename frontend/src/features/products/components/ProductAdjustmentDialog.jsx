import { useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
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
import { Label } from '@/shared/components/ui/label.jsx';
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/shared/components/ui/form.jsx';
import { ArrowDownCircle, ArrowRightLeft, ArrowUpCircle, Building2, ClipboardList, Package, LoaderCircle } from 'lucide-react';
import { cn } from '@/lib/utils';
import { createInventoryMovement } from '../services/inventoryService.js';
import { useProducts } from '../context/ProductsContext.jsx';

export function ProductAdjustmentDialog({ open, onOpenChange, product }) {
    const [activeTab, setActiveTab] = useState('entrada');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { updateProduct } = useProducts();

    const form = useForm({
        defaultValues: {
            quantity: '',
            supplier: '',
            cost: '',
            observations: '',
        },
    });

    useEffect(() => {
        if (open) {
            setActiveTab('entrada');
            form.reset({
                quantity: '',
                supplier: product?.proveedor || '',
                cost: '',
                observations: '',
            });
        }
    }, [open, product, form]);

    const currentStock = Number(product?.currentStock ?? product?.stock_actual ?? 0);
    const quantityValue = form.watch('quantity');
    const quantityNumber = Number.parseInt(quantityValue || '0', 10) || 0;
    const resultingStock = useMemo(() => {
        return activeTab === 'entrada'
            ? currentStock + quantityNumber
            : Math.max(0, currentStock - quantityNumber);
    }, [activeTab, currentStock, quantityNumber]);

    const handleClose = () => {
        form.reset();
        onOpenChange?.(false);
    };

    const onSubmit = async (data) => {
        if (!product?.id) {
            console.error('Product ID is required');
            return;
        }

        setIsSubmitting(true);
        try {
            const movementData = {
                productId: product.id,
                movementType: activeTab === 'entrada' ? 'IN' : 'OUT',
                quantity: Number.parseInt(data.quantity, 10),
                reason: data.observations || '',
                purchaseCost: Number.parseFloat(data.cost || '0'),
            };

            await createInventoryMovement(movementData);

            // Update the product stock in context
            const newStock = activeTab === 'entrada'
                ? currentStock + movementData.quantity
                : Math.max(0, currentStock - movementData.quantity);

            updateProduct(product.id, {
                ...product,
                currentStock: newStock,
                stock_actual: newStock,
            });

            handleClose();
        } catch (error) {
            console.error('Error creating inventory movement:', error);
            // You might want to show an error toast here
        } finally {
            setIsSubmitting(false);
        }
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
                                    {typeof product?.category === 'string' ? product.category : (product?.categoryObj?.name || product?.category || 'Sin categoría')}
                                </span>
                                {product?.proveedor && (
                                    <span className="flex items-center gap-2">
                                        <ClipboardList className="h-4 w-4 text-slate-700" />
                                        {product.proveedor}
                                    </span>
                                )}
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
                                    rules={{
                                        required: 'La cantidad es requerida',
                                        min: { value: 1, message: 'La cantidad debe ser mayor a 0' },
                                        validate: (value) => {
                                            const num = Number.parseInt(value, 10);
                                            if (isNaN(num) || num <= 0) {
                                                return 'La cantidad debe ser mayor a 0';
                                            }
                                            if (activeTab === 'salida' && num > currentStock) {
                                                return 'La cantidad no puede ser mayor al stock actual';
                                            }
                                            return true;
                                        },
                                    }}
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-gray-700">
                                                Cantidad a procesar <span className="text-red-500">*</span>
                                            </FormLabel>
                                            <FormControl>
                                                <Input
                                                    type="number"
                                                    min={0}
                                                    placeholder="Ingresar cantidad"
                                                    className="mt-2 bg-white"
                                                    {...field}
                                                    onChange={(e) => {
                                                        const value = e.target.value;
                                                        field.onChange(value);
                                                    }}
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
                                                Proveedor
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
                                    rules={{
                                        required: 'El costo de compra es requerido',
                                        min: { value: 0, message: 'El costo debe ser mayor o igual a 0' },
                                        validate: (value) => {
                                            const num = Number.parseFloat(value);
                                            if (isNaN(num) || num < 0) {
                                                return 'El costo debe ser mayor o igual a 0';
                                            }
                                            return true;
                                        },
                                    }}
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
                                <Button type="button" variant="outline" onClick={handleClose} disabled={isSubmitting}>
                                    Cancelar
                                </Button>
                                <Button type="submit" className="bg-slate-800 hover:bg-slate-900" disabled={isSubmitting}>
                                    {isSubmitting ? (
                                        <>
                                            <LoaderCircle className="mr-2 h-4 w-4 animate-spin" />
                                            Guardando...
                                        </>
                                    ) : (
                                        'Guardar movimiento'
                                    )}
                                </Button>
                            </div>
                        </form>
                    </Form>
                </div>
            </DialogContent>
        </Dialog>
    );
}


