import { FileUp, Plus, Printer, Search, X } from 'lucide-react';
import { Button } from "@/shared/components/ui/button.jsx";

export function CustomersFiltersBar({
    searchQuery,
    onSearchChange,
    onRegister,
    onExport
}) {

    return (
        <div className="flex gap-3 items-center mb-4 max-w-[1066px] h-10">
            <div className="relative flex-1 max-w-full">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
                <input
                    type="text"
                    aria-label="Buscar clientes"
                    value={searchQuery}
                    onChange={(e) => onSearchChange(e.target.value)}
                    placeholder="Buscar cliente por nombre, email o teléfono..."
                    className="input w-full border border-gray-300 rounded-lg pl-11 pr-10 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-transparent transition"
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
                onClick={onExport}
                className='bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm'
            >
                <FileUp className='h-4 w-4 mr-1' />
                Exportar
            </Button>
            <Button
                className='bg-[#436086] text-white cursor-pointer'
                onClick={onRegister}
            >
                <Plus className='h-4 w-4 mr-1' />
                Registrar cliente
            </Button>
        </div>
    );
}