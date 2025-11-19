import { Filter } from 'lucide-react';
import React from 'react';

export function ProductsFiltersBar({ searchQuery, onSearchChange, onToggleFilters }) {
    return (
        <div className="flex gap-3 items-center mb-4">
            <div className="flex-1">
                <input
                    aria-label="Buscar productos"
                    value={searchQuery}
                    onChange={(e) => onSearchChange(e.target.value)}
                    placeholder="Buscar por nombre, descripción..."
                    className="w-full border rounded px-3 py-2"
                />
            </div>

            <button
                type="button"
                onClick={onToggleFilters}
                className="px-3 py-2 border rounded bg-white"
            >
                Filtros
            </button>
        </div>
    );
}