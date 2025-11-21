import { useLocation, useNavigate } from 'react-router';
import { Button } from '@/shared/components/ui/button';
import { Plus } from 'lucide-react';
import { useProductsFilter } from '@/features/products/hooks/useProductsFilter';
import { ProductsFiltersBar } from '@/features/products/components/filters/ProductsFiltersBar.jsx';
import { ProductsFiltersPopup } from '../components/filters/ProductFiltersPopup';
import { ProductsTable } from "@/features/products/components/ProductsTable.jsx";
import {
  Dialog,
  DialogContent,
} from "@/shared/components/ui/dialog";
import { CreateProductComponent } from "../components/CreateProductComponent";

export default function ProductsListPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const isCreateModalOpen = location.pathname === '/products/create';
    
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

    const handleCreateModalChange = (open) => {
        if (!open) {
            navigate(-1);
        }
    };

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

            <Dialog open={isCreateModalOpen} onOpenChange={handleCreateModalChange}>
                <DialogContent className="max-h-[90vh] overflow-y-auto p-0 gap-0 max-w-3xl">
                    <div className="overflow-y-auto">
                        <CreateProductComponent />
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    );
}