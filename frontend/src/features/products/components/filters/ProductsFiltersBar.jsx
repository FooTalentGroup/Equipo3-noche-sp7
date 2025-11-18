import { Filter } from 'lucide-react';
import {SearchBar} from "@/features/products/components/filters/SearchBar.jsx";
import { Button } from '@/shared/components/ui/button.jsx';

export const ProductsFiltersBar = ({
                                       searchQuery,
                                       onSearchChange,
                                       onToggleFilters
                                   }) => {
    return (
        <div className='flex flex-col gap-3 sm:flex-row sm:items-center sm:gap-4 mb-6'>
            <div className='flex-1'>
                <SearchBar searchQuery={searchQuery} onSearchChange={onSearchChange} />
            </div>

            <div className='flex justify-end'>
                <Button
                    variant='outline'
                    onClick={onToggleFilters}
                    className='sm:w-auto bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm'
                >
                    <Filter className='h-4 w-4 mr-2' />
                    Filtros
                </Button>
            </div>
        </div>
    );
};