import { Search, Plus, Printer, Download } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';
import { SearchBar } from '@/features/products/components/filters/SearchBar.jsx';

export function CustomersFiltersBar({ searchQuery, onSearchChange, onRegister, onPrint, onExport }) {
    return (
        <div className="flex items-center gap-3 mb-4">
            <div className="flex-1">
                <SearchBar value={searchQuery} onChange={onSearchChange} placeholder="Buscar clientes..." />
            </div>

            <div className="flex gap-2">
                <Button onClick={onExport} className="bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm">
                    <Download className="h-4 w-4" /> Exportar
                </Button>

                <Button onClick={onPrint} className="bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm">
                    <Printer className="h-4 w-4" /> Imprimir
                </Button>

                <Button onClick={onRegister} className="bg-[#436086] text-white cursor-pointer">
                    <Plus className="h-4 w-4" /> Registrar
                </Button>
            </div>
        </div>
    );
}
