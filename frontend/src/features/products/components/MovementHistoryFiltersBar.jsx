import { Printer, Filter, Search, X } from 'lucide-react';
import { Button } from "@/shared/components/ui/button.jsx";

export function MovementHistoryFiltersBar({
    searchQuery,
    onSearchChange,
    onFilterClick,
    onExport,
    activeFiltersCount = 0
}) {
    return (
        <div className="flex gap-3 items-center mb-4 max-w-[1086px]">
            {/* Search Bar */}
            <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
                <input
                    type="text"
                    aria-label="Buscar producto"
                    value={searchQuery}
                    onChange={(e) => onSearchChange(e.target.value)}
                    placeholder="¿Qué producto estás buscando hoy?"
                    className="w-full border border-gray-300 rounded-lg pl-11 pr-10 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-transparent transition"
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

            {/* Filter Button */}
            <Button
                variant="outline"
                onClick={onFilterClick}
                className="relative cursor-pointer text-[#0A0A0A]"
            >
                <Filter className="h-4 w-4 mr-1" />
                Filtrar
                {activeFiltersCount > 0 && (
                    <span className="absolute -top-1 -right-1 bg-[#436086] text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                        {activeFiltersCount}
                    </span>
                )}
            </Button>

            {/* Export Button */}
            <Button
                onClick={onExport}
                variant="outline"
                className="cursor-pointer text-[#0A0A0A]"
            >
                <Printer className="h-4 w-4 mr-1" />
                Exportar
            </Button>
        </div>
    );
}
