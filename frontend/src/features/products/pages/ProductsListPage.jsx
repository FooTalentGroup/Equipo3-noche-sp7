import { useState } from 'react';
import { Button } from '@/shared/components/ui/button';
import { Plus } from 'lucide-react';
import { useProductsFilter } from '@/features/products/hooks/useProductsFilter';
import { ProductsFiltersBar } from '@/features/products/components/filters/ProductsFiltersBar.jsx';
import { ProductsFiltersPopup } from '../components/filters/ProductFiltersPopup';
import { ProductsTable } from "@/features/sales/components/ProductsTable.jsx";

export default function ProductsListPage() {
    const {
        searchQuery,
        setSearchQuery,
        filters,
        setFilters,
        sort,
        setSort,
        isFiltersOpen,
        openFilters,
        closeFilters,
        applyFilters,
        clearFilters,
    } = useProductsFilter();

    return (
        <div className="p-6 w-full">
            <ProductsFiltersBar
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                onToggleFilters={openFilters}
            />
            <ProductsTable
                searchQuery={searchQuery}
                filters={filters}
                sort={sort}
            />

            <ProductsFiltersPopup
                open={isFiltersOpen}
                onClose={closeFilters}
                filters={filters}
                onChange={setFilters}
                sort={sort}
                onSortChange={setSort}
                onApply={() => {
                    applyFilters();
                    closeFilters();
                }}
                onClear={() => {
                    clearFilters();
                }}
            />
        </div>
    );
}