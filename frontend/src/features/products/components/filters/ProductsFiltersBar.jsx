import { FileUp, Funnel, Plus, ArrowDownUp, X, SearchIcon } from 'lucide-react';
import React from 'react';
import { Button } from "@/shared/components/ui/button.jsx";
import { useNavigate } from 'react-router';
import { useProducts } from '../../context/ProductsContext';
import { Input } from '@/shared/components/ui/input';
import { InputGroup, InputGroupAddon, InputGroupButton, InputGroupInput } from '@/shared/components/ui/input-group';

export function ProductsFiltersBar({ onToggleFilters }) {
    const navigate = useNavigate();
    const { activeFiltersCount, filters, setSearch } = useProducts();
    return (
        <>
            <div className="flex gap-3 items-center mb-4 max-w-[1066px] h-10">
                <div className="relative flex-1 max-w-2xl">
                    <InputGroup>
                        <InputGroupInput placeholder="¿Qué producto estás buscando hoy?" value={filters.q}
                            onChange={(e) => setSearch(e.target.value)} />
                        <InputGroupAddon>
                            <SearchIcon />
                        </InputGroupAddon>
                       {filters.q && <InputGroupButton
                            onClick={() => setSearch("")}
                            size="icon-xs"
                        >
                            <X></X>
                        </InputGroupButton>}
                    </InputGroup>
                </div>

                <Button
                    onClick={onToggleFilters}
                    variant={'secondary'}
                >
                    <Funnel className='h-4 w-4 mr-1' fill={activeFiltersCount > 0 ? "currentColor" : "none"} />
                    Filtrar
                </Button>
                <Button
                    variant={'secondary'}
                >
                    <FileUp className='h-4 w-4 mr-1' />
                    Exportar
                </Button>
                <Button
                    variant={'secondary'}
                    onClick={() => navigate('/inventory-movements')}
                >
                    <ArrowDownUp className='h-4 w-4 mr-1' />
                    Historial de movimientos
                </Button>
                <Button
                    variant={'stokia'}
                    onClick={() => navigate('/products/create')}
                >
                    <Plus className='h-4 w-4 mr-1' />
                    Registrar
                </Button>
            </div>
        </>
    );
}