import { useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
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
import { ArrowDown, ArrowUp } from 'lucide-react';
import { cn } from '@/lib/utils';
import { createInventoryMovement, getInventoryMovementsByProduct } from '../services/inventoryService.js';
import { useProducts } from '../context/ProductsContext.jsx';
import { InventoryLoadingModal } from '@/shared/components/ui/InventoryLoadingModal.jsx';
import { InventorySuccessModal } from '@/shared/components/ui/InventorySuccessModal.jsx';

export function ProductAdjustmentDialog({ open, onOpenChange, product }) {
    const [activeTab, setActiveTab] = useState('entrada');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const { updateProduct } = useProducts();
    const navigate = useNavigate();

    const form = useForm({
        defaultValues: {
            quantity: '',
            purchaseCost: '',
            reason: '',
        },
    });

    useEffect(() => {
        if (open && product?.id) {
            setActiveTab('entrada');
            setShowSuccess(false);

            // Fetch last inventory movement to prefill data
            const fetchLastMovement = async () => {
                try {
                    const movements = await getInventoryMovementsByProduct(product.id);
                    if (movements && movements.length > 0) {
                        // Get the most recent movement
                        const lastMovement = movements[0];
                        form.reset({
                            quantity: lastMovement.quantity?.toString() || '',
                            purchaseCost: lastMovement.purchaseCost?.toString() || '',
                            reason: '',
                        });
                    } else {
                        // No previous movements, use empty defaults
                        form.reset({
                            quantity: '',
                            purchaseCost: '',
                            reason: '',
                        });
                    }
                } catch (error) {
                    console.error('Error fetching last movement:', error);
                    // On error, use empty defaults
                    form.reset({
                        quantity: '',
                        purchaseCost: '',
                        reason: '',
                    });
                }
            };

            fetchLastMovement();
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
        setShowSuccess(false);
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
                reason: data.reason || '',
                purchaseCost: Number.parseFloat(data.purchaseCost || '0'),
            };

            const result = await createInventoryMovement(movementData);
            console.log('✅ Inventory movement created successfully:', result);

            // Update the product stock in context
            const newStock = activeTab === 'entrada'
                ? currentStock + movementData.quantity
                : Math.max(0, currentStock - movementData.quantity);

            updateProduct(product.id, {
                ...product,
                currentStock: newStock,
                stock_actual: newStock,
            });

            setIsSubmitting(false);
            onOpenChange?.(false); // Close the dialog
            setShowSuccess(true);

            // Redirect after 5 seconds
            setTimeout(() => {
                setShowSuccess(false);
                navigate('/inventory-movements');
            }, 5000);

        } catch (error) {
            console.error('❌ Error creating inventory movement:', error);
            setIsSubmitting(false);
            // You might want to show an error toast here
        }
    };

    return (
        <>
            <Dialog open={open} onOpenChange={onOpenChange}>
                <DialogContent className="max-w-3xl p-0 overflow-hidden">
                    <DialogHeader className="px-8 pt-8 mb-6">
                        <DialogTitle className="text-[30px] font-semibold text-[#111827]">
                            Ajuste de Inventario
                        </DialogTitle>
                        <DialogDescription className="text-[20px] font-medium text-[#171717]">
                            {product?.name || 'Producto sin nombre'}
                        </DialogDescription>
                    </DialogHeader>

                    <div className="px-8 pb-8">
                        <div className="rounded-2xl border border-slate-200 bg-[#F9FAFB] p-6 mb-8">
                            <div className="flex items-center justify-between">
                                <div>
                                    <p className="text-[#6B7280] text-[1rem] font-medium">
                                        Stock actual en almacén
                                    </p>
                                    <p className="text-[30px] font-semibold text-[#111827]">
                                        {currentStock}
                                        <span className="text-[14px] font-normal text-[#9CA3AF] ml-2">unid.</span>
                                    </p>
                                </div>
                                <div className="flex flex-col gap-2 text-sm text-slate-600">
                                    <span className="flex items-center gap-2">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 32 32" fill="none">
                                            <path d="M4 4V25.3333C4 26.0406 4.28095 26.7189 4.78105 27.219C5.28115 27.719 5.95942 28 6.66667 28H28" stroke="#D1D5DB" strokeWidth="2.66667" strokeLinecap="round" strokeLinejoin="round" />
                                            <path d="M24 22.6667V12" stroke="#D1D5DB" strokeWidth="2.66667" strokeLinecap="round" strokeLinejoin="round" />
                                            <path d="M17.3333 22.6665V6.6665" stroke="#D1D5DB" strokeWidth="2.66667" strokeLinecap="round" strokeLinejoin="round" />
                                            <path d="M10.6667 22.6665V18.6665" stroke="#D1D5DB" strokeWidth="2.66667" strokeLinecap="round" strokeLinejoin="round" />
                                        </svg>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div className="bg-slate-100 rounded-xl p-1 mb-6 flex gap-2">
                            <button
                                type="button"
                                className={cn(
                                    'flex-1 cursor-pointer flex items-center justify-center gap-2 px-4 py-3 rounded-lg text-sm font-medium transition',
                                    activeTab === 'entrada'
                                        ? 'bg-[#436086] text-white shadow'
                                        : 'text-slate-600 hover:text-slate-900'
                                )}
                                onClick={() => setActiveTab('entrada')}
                            >
                                <ArrowDown className="h-4 w-4" />
                                Entrada (Compra)
                            </button>
                            <button
                                type="button"
                                className={cn(
                                    'flex-1 cursor-pointer flex items-center justify-center gap-2 px-4 py-3 rounded-lg text-sm font-medium transition',
                                    activeTab === 'salida'
                                        ? 'bg-[#436086] text-white shadow'
                                        : 'text-slate-600 hover:text-slate-900'
                                )}
                                onClick={() => setActiveTab('salida')}
                            >
                                <ArrowUp className="h-4 w-4" />
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
                                                <FormLabel className="text-[#374151] text-[1rem]">
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
                                    <div className="flex items-center gap-2 text-[#202326] text-[1rem] font-normal">
                                        Stock resultante:
                                        <span className="text-lg font-semibold text-[#202326]">
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
                                        name="purchaseCost"
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
                                    name="reason"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-gray-700">
                                                Motivo / Observaciones
                                            </FormLabel>
                                            <FormControl>
                                                <Textarea
                                                    rows={4}
                                                    placeholder="Agregar motivo / observaciones"
                                                    className="mt-2 bg-white resize-none text-[#0A0A0A]"
                                                    {...field}
                                                />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />

                                <div className="flex flex-col md:flex-row gap-3 pt-4">
                                    <Button type="button" className="cursor-pointer" variant="outline" onClick={handleClose} disabled={isSubmitting}>
                                        Cancelar
                                    </Button>
                                    <Button type="submit" className="bg-[#436086] cursor-pointer hover:bg-slate-900 py-[9.5px] px-6" disabled={isSubmitting}>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                            <path d="M9.09863 0.0126953C9.57239 0.0662879 10.016 0.281516 10.3525 0.625L10.3516 0.625977L12.874 3.14746C13.2666 3.53211 13.4922 4.05693 13.5 4.60645V11.417C13.4999 11.9694 13.2803 12.499 12.8896 12.8896C12.499 13.2803 11.9694 13.4999 11.417 13.5H2.08301C1.53059 13.4999 1.00098 13.2803 0.610352 12.8896C0.219727 12.499 8.61062e-05 11.9694 0 11.417V2.08301C8.62008e-05 1.53059 0.219727 1.00098 0.610352 0.610352C1.00098 0.219727 1.53059 8.62008e-05 2.08301 0H8.89355L9.09863 0.0126953ZM2.08301 1.5C1.92842 1.50009 1.78022 1.56158 1.6709 1.6709C1.56158 1.78022 1.50009 1.92842 1.5 2.08301V11.417C1.50009 11.5716 1.56158 11.7198 1.6709 11.8291C1.78022 11.9384 1.92842 11.9999 2.08301 12H2.66699V8.08398C2.66699 7.70826 2.81636 7.34771 3.08203 7.08203C3.34771 6.81636 3.70826 6.66699 4.08398 6.66699H9.41699C9.74564 6.66707 10.0624 6.78179 10.3145 6.98828L10.4189 7.08203L10.5127 7.18555C10.7192 7.43762 10.834 7.75528 10.834 8.08398V12H11.417C11.5716 11.9999 11.7198 11.9384 11.8291 11.8291C11.9384 11.7198 11.9999 11.5716 12 11.417V4.62695C11.9977 4.47323 11.935 4.32636 11.8252 4.21875L11.8193 4.21387L9.28613 1.68066L9.28125 1.6748C9.2005 1.5924 9.09753 1.53647 8.98633 1.5127L8.87305 1.5H4.16699V3.33301H8.75C9.16396 3.33318 9.49982 3.66905 9.5 4.08301C9.5 4.49711 9.16406 4.83283 8.75 4.83301H4.08398C3.70834 4.83301 3.34769 4.68354 3.08203 4.41797C2.81653 4.15238 2.66708 3.79252 2.66699 3.41699V1.5H2.08301ZM4.16699 12H9.33398V8.16699H4.16699V12Z" fill="#FAFAFA" />
                                        </svg>
                                        Guardar movimiento
                                    </Button>
                                </div>
                            </form>
                        </Form>
                    </div>
                </DialogContent>
            </Dialog>

            {isSubmitting && <InventoryLoadingModal />}
            {showSuccess && <InventorySuccessModal />}
        </>
    );
}


