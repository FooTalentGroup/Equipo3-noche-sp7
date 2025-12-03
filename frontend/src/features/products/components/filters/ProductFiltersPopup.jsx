import React from 'react';
import { useProducts } from '../../context/ProductsContext';
import { useCategories } from '@/features/categories/context/CategoriesContext';
import { NativeSelect } from '@/shared/components/ui/native-select';
import { Label } from '@/shared/components/ui/label';
import { Loader2 } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';
import { Dialog, DialogContent, DialogTitle } from '@/shared/components/ui/dialog';
import { Description } from '@radix-ui/react-dialog';

const STOCK_OPTIONS = [
    { value: 'all', label: 'Todos los estados' },
    { value: 'low', label: 'Bajo stock' },
    { value: 'normal', label: 'Stock normal' },
];

const SORT_OPTIONS = [
    { value: 'name,asc', label: 'Nombre (A → Z)' },
    { value: 'name,desc', label: 'Nombre (Z → A)' },
    { value: 'price,asc', label: 'Precio (Menor → Mayor)' },
    { value: 'price,desc', label: 'Precio (Mayor → Menor)' },
    { value: 'currentStock,asc', label: 'Stock (Menor → Mayor)' },
    { value: 'currentStock,desc', label: 'Stock (Mayor → Menor)' },
];

const DELETED_OPTIONS = [
    { value: 'all', label: 'Todos' },
    { value: 'true', label: 'Eliminados' },
    { value: 'false', label: 'Activos' },
];

export function ProductsFiltersPopup({ open, onClose }) {
    const { filters, updateFilters, clearFilters, setStockFilter, stockFilterValue, setDeleted } = useProducts();
    const { categories, isFetching } = useCategories();

    if (!open) return null;

    function update(field, value) {
        updateFilters({ [field]: value || undefined }); 
    }

    function clearAll() {
        clearFilters();
    }

    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogTitle className="sr-only">
                Filtros y opciones de orden para la lista de productos
            </DialogTitle>
            <Description className="sr-only">
                Filtros y opciones de orden para la lista de productos
            </Description>
            <DialogContent className="overflow-y-auto p-12 gap-6 max-w-lg bg-stokia-neutral-50">
                <h3 className="text-lg font-semibold">Filtros y orden</h3>

                <div className="space-y-4 [&_span]:text-stokia-neutral-950">
                    <Label className="block relative">
                        <span className="text-sm font-medium">Categoría</span>
                        <NativeSelect
                            onChange={(e) => update('categoryId', e.target.value)}
                            value={filters.categoryId || ''}
                            disabled={isFetching}
                            className="bg-card"
                        >
                            <option value="">
                                {isFetching ? "Cargando..." : "Todas las categorías"}
                            </option>
                            {!isFetching && categories.map((opt) => (
                                <option key={opt.id} value={String(opt.id)}>
                                    {opt.name}
                                </option>
                            ))}
                        </NativeSelect>
                        {isFetching && (
                            <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none">
                                <Loader2 className="h-4 w-4 animate-spin text-stokia-neutral-400" />
                            </div>
                        )}
                    </Label>

                    <Label className="block">
                        <span className="text-sm">Estado de stock</span>
                        <NativeSelect
                            onChange={(e) => setStockFilter(e.target.value)}
                            value={stockFilterValue}
                            className="bg-card"
                        >
                            {STOCK_OPTIONS.map((opt) => (
                                <option key={opt.value} value={opt.value}>
                                    {opt.label}
                                </option>
                            ))}
                        </NativeSelect>
                    </Label>

                     <Label className="block">
                        <span className="text-sm">Estado</span>
                        <NativeSelect
                            value={filters.deleted || 'all'}
                            onChange={(e) => setDeleted(e.target.value)}
                            className="bg-card"
                        >
                            {DELETED_OPTIONS.map((opt) => (
                                <option key={opt.value} value={opt.value}>
                                    {opt.label}
                                </option>
                            ))}
                        </NativeSelect>
                    </Label>

                    <Label className="block">
                        <span className="text-sm">Ordenar por</span>
                        <NativeSelect
                            value={filters.sort || 'name,asc'}
                            onChange={(e) => update('sort', e.target.value)}
                            className="bg-card"
                        >
                            {SORT_OPTIONS.map((opt) => (
                                <option key={opt.value} value={opt.value}>
                                    {opt.label}
                                </option>
                            ))}
                        </NativeSelect>
                    </Label>
                </div>

                <div className="flex gap-3 justify-end">
                    <Button variant="outline" onClick={clearAll}>
                        Limpiar
                    </Button>

                    <Button onClick={onClose} variant="outline">
                        Cancelar
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    );
}