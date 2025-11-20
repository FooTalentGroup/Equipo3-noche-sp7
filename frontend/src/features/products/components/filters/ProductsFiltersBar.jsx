import {FileUp, Filter, Funnel, Plus, Printer} from 'lucide-react';
import React from 'react';
import {Button} from "@/shared/components/ui/button.jsx";

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

            <Button
                onClick={onToggleFilters}
                className='bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm'
            >
                <Funnel className='h-4 w-4 mr-1' />
                Filtrar
            </Button>
                <Button
                    className='bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm'
                >
                    <FileUp className='h-4 w-4 mr-1' />
                    Exportar
                </Button>
                <Button
                    className='bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm'
                >
                    <Printer className='h-4 w-4 mr-1' />
                    Imprimir
                </Button>
                <Button
                    className='bg-[#436086] text-white cursor-pointer'
                >
                    <Plus className='h-4 w-4 mr-1' />
                    Registrar
                </Button>


        </div>
    );
}