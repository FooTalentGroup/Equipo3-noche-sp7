import React, { useState, useEffect } from 'react';

export function CreateClientPopup({
                                         open,
                                         onClose,
                                         filters,
                                         onChange,
                                         sort,
                                         onSortChange,
                                         onApply,
                                         onClear,
                                     }) {
    const initialLocal = {
        category: filters?.category ?? 'all',
        minPrice: filters?.minPrice ?? '',
        maxPrice: filters?.maxPrice ?? '',
        // NUEVO: stockLevel → 'all' | 'high' | 'medium' | 'low'
        stockLevel: filters?.stockLevel ?? 'all',
    };

    const [local, setLocal] = useState(initialLocal);

    // Sync cuando cambian los filtros externos o se abre el popup
    useEffect(() => {
        setLocal({
            category: filters?.category ?? 'all',
            minPrice: filters?.minPrice ?? '',
            maxPrice: filters?.maxPrice ?? '',
            stockLevel: filters?.stockLevel ?? 'all',
        });
    }, [filters, open]);

    if (!open) return null;

    function update(field, value) {
        setLocal(prev => ({ ...prev, [field]: value }));
    }

    function applyAll() {
        onChange(local);           // enviamos stockLevel directamente
        if (onApply) onApply();
    }

    function clearAll() {
        const cleared = {
            category: 'all',
            minPrice: '',
            maxPrice: '',
            stockLevel: 'all',
        };
        setLocal(cleared);
        onClear && onClear();
        onChange && onChange(cleared);
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
            <div className="absolute inset-0 bg-black/40" onClick={onClose} />
            <div className="relative bg-white rounded-lg shadow-xl w-96 p-6 z-10">
                <h3 className="text-lg font-semibold mb-4">Filtros y orden</h3>

                <div className="space-y-4">
                    {/* Categoría */}
                    <label className="block">
                        <span className="text-sm font-medium text-gray-700">Categoría</span>
                        <select
                            value={local.category}
                            onChange={(e) => update('category', e.target.value)}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary sm:text-sm h-10 px-3"
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

                    {/* Precio mínimo */}
                    <label className="block">
                        <span className="text-sm font-medium text-gray-700">Precio mínimo</span>
                        <input
                            type="number"
                            value={local.minPrice}
                            onChange={(e) => update('minPrice', e.target.value)}
                            placeholder="Ej: 1000"
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary h-10 px-3"
                            min="0"
                        />
                    </label>

                    {/* Precio máximo */}
                    <label className="block">
                        <span className="text-sm font-medium text-gray-700">Precio máximo</span>
                        <input
                            type="number"
                            value={local.maxPrice}
                            onChange={(e) => update('maxPrice', e.target.value)}
                            placeholder="Ej: 10000"
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary h-10 px-3"
                            min="0"
                        />
                    </label>

                    {/* NUEVO: Estado de stock (Alto / Medio / Bajo) */}
                    <label className="block">
                        <span className="text-sm font-medium text-gray-700">Estado de stock</span>
                        <select
                            value={local.stockLevel}
                            onChange={(e) => update('stockLevel', e.target.value)}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary sm:text-sm h-10 px-3"
                        >
                            <option value="all">Todos los estados</option>
                            <option value="high">Alto stock (más de 10 und)</option>
                            <option value="medium">Medio stock (1 a 10 und)</option>
                            <option value="low">Bajo stock (0 und)</option>
                        </select>
                    </label>

                    {/* Ordenar por */}
                    <label className="block">
                        <span className="text-sm font-medium text-gray-700">Ordenar por</span>
                        <select
                            value={sort}
                            onChange={(e) => onSortChange(e.target.value)}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary sm:text-sm h-10 px-3"
                        >
                            <option value="name_asc">Nombre (A → Z)</option>
                            <option value="name_desc">Nombre (Z → A)</option>
                            <option value="price_asc">Precio (Menor → Mayor)</option>
                            <option value="price_desc">Precio (Mayor → Menor)</option>
                        </select>
                    </label>
                </div>

                <div className="mt-6 flex gap-3 justify-end">
                    <button
                        onClick={clearAll}
                        className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                        Limpiar
                    </button>
                    <button
                        onClick={onClose}
                        className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                        Cancelar
                    </button>
                    <button
                        onClick={applyAll}
                        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                        Aplicar filtros
                    </button>
                </div>
            </div>
        </div>
    );
}