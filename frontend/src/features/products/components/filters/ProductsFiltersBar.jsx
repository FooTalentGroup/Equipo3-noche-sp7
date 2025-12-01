import { FileUp, Filter, Funnel, Plus, ArrowDownUp, Search, X } from 'lucide-react';
import React from 'react';
import { Button } from "@/shared/components/ui/button.jsx";
import { useNavigate } from 'react-router';

export function ProductsFiltersBar({ searchQuery, onSearchChange, onToggleFilters }) {
    const navigate = useNavigate();
    return (
        <>
            <div className="flex gap-3 items-center mb-4 max-w-[1066px] h-10">
                <div className="relative flex-1 max-w-2xl">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
                    <input
                        type="text"
                        aria-label="Buscar productos"
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                        placeholder="¿Qué producto estás buscando hoy?"
                        className="wnput w-full border border-gray-300 rounded-lg pl-11 pr-10 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-transparent transition"
                    />
                    {searchQuery && (
                        <button
                            onClick={() => onSearchChange('')}
                            className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                            aria-label="Limpiar búsqueda"
                        >
                            <X className="h-4 w-4" />
                        </button>
                    )}
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
                    onClick={() => navigate('/inventory-movements')}
                >
                    <ArrowDownUp className='h-4 w-4 mr-1' />
                    Historial de movimientos
                </Button>
                <Button
                    className='bg-[#436086] text-white cursor-pointer'
                    onClick={() => navigate('/products/create')}
                >
                    <Plus className='h-4 w-4 mr-1' />
                    Registrar producto
                </Button>
            </div>
        </>
    );
}