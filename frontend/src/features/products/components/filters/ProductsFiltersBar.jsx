import { FileUp, Funnel, Plus, ArrowDownUp, X, SearchIcon } from 'lucide-react';
import React, { useState } from 'react';
import { Button } from "@/shared/components/ui/button.jsx";
import { useNavigate } from 'react-router';
import { useProducts } from '../../context/ProductsContext';
import { InputGroup, InputGroupAddon, InputGroupButton, InputGroupInput } from '@/shared/components/ui/input-group';
import generarReportePDF from '../../utils/pdfReport';
import { getProducts } from '../../services/productService';

export function ProductsFiltersBar({ onToggleFilters }) {
    const navigate = useNavigate();
    const { activeFiltersCount, filters, setSearch } = useProducts();

    const [exporting, setExporting] = useState(false);

    const handleExport = async () => {
        try {
            setExporting(true);
            const res = await getProducts({ size: 1000 });
            generarReportePDF(res.data.content);
        } finally {
            setExporting(false);
        }
    };

    return (
        <div className="flex gap-3 items-center mb-4 max-w-[1066px] h-10">
            <div className="relative flex-1 max-w-2xl">
                <InputGroup>
                    <InputGroupInput
                        placeholder="¿Qué producto estás buscando hoy?"
                        value={filters.q}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    <InputGroupAddon>
                        <SearchIcon />
                    </InputGroupAddon>
                    {filters.q && (
                        <InputGroupButton
                            onClick={() => setSearch("")}
                            size="icon-xs"
                        >
                            <X />
                        </InputGroupButton>
                    )}
                </InputGroup>
            </div>

            <Button onClick={onToggleFilters} variant="secondary" className={`hover:bg-stokia-neutral-300`}>
                <Funnel
                    className="h-4 w-4 mr-1"
                    fill={activeFiltersCount > 0 ? "currentColor" : "none"}
                />
                Filtrar
            </Button>

            <Button
                variant="secondary"
                onClick={handleExport}
                disabled={exporting}
                className={`hover:bg-stokia-neutral-300 ${exporting ? 'bg-stokia-neutral-100' : ''}`}
            >
                <FileUp className="h-4 w-4 mr-1" />
                {exporting ? "Generando..." : "Exportar"}
            </Button>

            <Button
                variant="secondary"
                onClick={() => navigate('/inventory-movements')}
                className={`hover:bg-stokia-neutral-300`}
            >
                <ArrowDownUp className="h-4 w-4 mr-1" />
                Historial de movimientos
            </Button>

            <Button
                variant="stokia"
                onClick={() => navigate('/products/create')}
                className={`hover:bg-stokia-neutral-300`}
            >
                <Plus className="h-4 w-4 mr-1" />
                Registrar
            </Button>
        </div>
    );
}
