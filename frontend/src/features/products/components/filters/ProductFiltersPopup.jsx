// ...existing code...
import React, { useState, useEffect } from 'react';

export function ProductsFiltersPopup({
    open,
    onClose,
    filters,
    onChange,
    sort,
    onSortChange,
    onApply,
    onClear,
}) {
    // initialize local with stockStatus compatibility
    const initialLocal = {
        category: filters?.category ?? 'all',
        minPrice: filters?.minPrice ?? '',
        maxPrice: filters?.maxPrice ?? '',
        // support existing boolean inStock for backward compatibility
        stockStatus: filters?.stockStatus ?? (filters?.inStock ? 'in' : 'all'),
    };

    const [local, setLocal] = useState(initialLocal);

    useEffect(() => {
        setLocal({
            category: filters?.category ?? 'all',
            minPrice: filters?.minPrice ?? '',
            maxPrice: filters?.maxPrice ?? '',
            stockStatus: filters?.stockStatus ?? (filters?.inStock ? 'in' : 'all'),
        });
    }, [filters, open]);

    if (!open) return null;

    function update(field, value) {
        const next = { ...local, [field]: value };
        setLocal(next);
    }

    function applyAll() {
        // keep compatibility: add inStock boolean derived from stockStatus
        const out = { ...local, inStock: local.stockStatus === 'in' };
        onChange(out);
        if (onApply) onApply();
    }

    function clearAll() {
        const cleared = { category: 'all', minPrice: '', maxPrice: '', stockStatus: 'all' };
        setLocal(cleared);
        // keep compatibility: include inStock false
        onClear && onClear();
        onChange && onChange({ ...cleared, inStock: false });
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
            <div className="absolute inset-0 bg-black/40" onClick={onClose} />
            <div className="relative bg-white rounded shadow-lg w-96 p-4 z-10">
                <h3 className="text-lg font-semibold mb-3">Filtros y orden</h3>

                <div className="space-y-3">
                    <label className="block">
                        Categoría
                        <select
                            value={local.category}
                            onChange={(e) => update('category', e.target.value)}
                            className="w-full border rounded px-2 py-1 mt-1"
                        >
                            <option value="all">Todas</option>
                            <option value="Bebidas">Bebidas</option>
                            <option value="Lácteos">Lácteos</option>
                            <option value="Panadería">Panadería</option>
                            <option value="Frutas y Verduras">Frutas y Verduras</option>
                            <option value="Desayuno">Desayuno</option>
                            <option value="Almacén">Almacén</option>
                            <option value="Limpieza">Limpieza</option>
                            <option value="Cuidado Personal">Cuidado Personal</option>
                        </select>
                    </label>

                    <label className="block">
                        Precio mínimo
                        <input
                            type="number"
                            value={local.minPrice}
                            onChange={(e) => update('minPrice', e.target.value)}
                            className="w-full border rounded px-2 py-1 mt-1"
                            min="0"
                        />
                    </label>

                    <label className="block">
                        Precio máximo
                        <input
                            type="number"
                            value={local.maxPrice}
                            onChange={(e) => update('maxPrice', e.target.value)}
                            className="w-full border rounded px-2 py-1 mt-1"
                            min="0"
                        />
                    </label>

                    <label className="block">
                        Estado de stock
                        <select
                            value={local.stockStatus}
                            onChange={(e) => update('stockStatus', e.target.value)}
                            className="w-full border rounded px-2 py-1 mt-1"
                        >
                            <option value="all">Todos</option>
                            <option value="in">Solo en stock</option>
                            <option value="out">Solo agotados</option>
                        </select>
                    </label>

                    <label className="block">
                        Ordenar por
                        <select
                            value={sort}
                            onChange={(e) => onSortChange(e.target.value)}
                            className="w-full border rounded px-2 py-1 mt-1"
                        >
                            <option value="name_asc">Nombre (A → Z)</option>
                            <option value="name_desc">Nombre (Z → A)</option>
                            <option value="price_asc">Precio (Menor → Mayor)</option>
                            <option value="price_desc">Precio (Mayor → Menor)</option>
                        </select>
                    </label>
                </div>

                <div className="mt-4 flex gap-2 justify-end">
                    <button className="px-3 py-1 border rounded" onClick={clearAll}>Limpiar</button>
                    <button className="px-3 py-1 border rounded" onClick={onClose}>Cerrar</button>
                    <button className="px-3 py-1 bg-primary text-white rounded" onClick={applyAll}>Aplicar</button>
                </div>
            </div>
        </div>
    );
}
