import { useEffect, useMemo, useState } from 'react';
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
import { ArrowDownCircle, ArrowRightLeft, ArrowUpCircle, Building2, ClipboardList, Package } from 'lucide-react';
import { cn } from '@/lib/utils';

const defaultFormState = {
    quantity: '',
    supplier: '',
    cost: '',
    observations: '',
};

export function ProductAdjustmentDialog({ open, onOpenChange, product }) {
    const [formState, setFormState] = useState(defaultFormState);
    const [activeTab, setActiveTab] = useState('entrada');

    useEffect(() => {
        if (open) {
            setActiveTab('entrada');
            setFormState({
                quantity: '',
                supplier: product?.proveedor || '',
                cost: '',
                observations: '',
            });
        }
    }, [open, product]);

    const currentStock = Number(product?.stock_actual ?? 0);
    const quantityNumber = Number.parseInt(formState.quantity || '0', 10) || 0;
    const resultingStock = useMemo(() => {
        return activeTab === 'entrada'
            ? currentStock + quantityNumber
            : Math.max(0, currentStock - quantityNumber);
    }, [activeTab, currentStock, quantityNumber]);

    const handleChange = (field) => (event) => {
        const value = event.target.value;
        setFormState((prev) => ({ ...prev, [field]: value }));
    };

    const handleClose = () => {
        onOpenChange?.(false);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // TODO: hook into backend mutation once available
        handleClose();
    };

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
                                    {product?.category || 'Sin categoría'}
                                </span>
                                <span className="flex items-center gap-2">
                                    <ClipboardList className="h-4 w-4 text-slate-700" />
                                    {product?.proveedor || 'Proveedor no registrado'}
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

                    <form className="space-y-6" onSubmit={handleSubmit}>
                        <div className="grid grid-cols-1 md:grid-cols-[minmax(0,1fr)_auto] gap-6 items-end">
                            <div>
                                <Label htmlFor="quantity-input" className="text-gray-700">
                                    Cantidad a procesar <span className="text-red-500">*</span>
                                </Label>
                                <Input
                                    id="quantity-input"
                                    type="number"
                                    min={0}
                                    required
                                    placeholder="Ingresar cantidad"
                                    value={formState.quantity}
                                    onChange={handleChange('quantity')}
                                    className="mt-2 bg-white"
                                />
                            </div>
                            <div className="flex items-center gap-2 text-gray-700">
                                <ArrowRightLeft className="h-4 w-4 text-slate-600" />
                                Stock resultante:
                                <span className="text-lg font-semibold text-slate-900">
                                    {resultingStock}
                                </span>
                            </div>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <Label htmlFor="supplier-input" className="text-gray-700">
                                    Proveedor <span className="text-red-500">*</span>
                                </Label>
                                <Input
                                    id="supplier-input"
                                    required
                                    placeholder="Colocar nombre del proveedor"
                                    value={formState.supplier}
                                    onChange={handleChange('supplier')}
                                    className="mt-2 bg-white"
                                />
                            </div>
                            <div>
                                <Label htmlFor="cost-input" className="text-gray-700">
                                    Costo de compra <span className="text-red-500">*</span>
                                </Label>
                                <Input
                                    id="cost-input"
                                    type="number"
                                    min={0}
                                    step="0.01"
                                    required
                                    placeholder="Colocar costo de compra"
                                    value={formState.cost}
                                    onChange={handleChange('cost')}
                                    className="mt-2 bg-white"
                                />
                            </div>
                        </div>

                        <div>
                            <Label htmlFor="observations-input" className="text-gray-700">
                                Motivo / Observaciones
                            </Label>
                            <Textarea
                                id="observations-input"
                                rows={4}
                                placeholder="Agregar motivo / observaciones"
                                value={formState.observations}
                                onChange={handleChange('observations')}
                                className="mt-2 bg-white resize-none"
                            />
                        </div>

                        <div className="flex flex-col md:flex-row justify-end gap-3 pt-4">
                            <Button type="button" variant="outline" onClick={handleClose}>
                                Cancelar
                            </Button>
                            <Button type="submit" className="bg-slate-800 hover:bg-slate-900">
                                Guardar movimiento
                            </Button>
                        </div>
                    </form>
                </div>
            </DialogContent>
        </Dialog>
    );
}


