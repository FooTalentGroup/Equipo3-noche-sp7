import { useState } from 'react';
import { Button } from '@/shared/components/ui/button';
import { Plus } from 'lucide-react';
import {ProductsTable} from "@/features/sales/components/ProductsTable.jsx";
import { ProductsFiltersBar } from '@/features/products/components/filters/ProductsFiltersBar.jsx';

export default function ProductsListPage() {
    const [searchQuery, setSearchQuery] = useState('');
    return (
        <div className='p-6 w-full'>
            <ProductsFiltersBar
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                onToggleFilters={() => {
                    console.log('Toggle filtros');
                }}
            />
            <ProductsTable searchQuery={searchQuery} />
        </div>
    );
}