import { useLocation, useNavigate } from 'react-router';
import { useProductsFilter } from '@/features/products/hooks/useProductsFilter';
import { ProductsFiltersBar } from '@/features/products/components/filters/ProductsFiltersBar.jsx';
import { ProductsFiltersPopup } from '../components/filters/ProductFiltersPopup';
import { ProductsTable } from "@/features/products/components/ProductsTable.jsx";
import {
    Dialog,
    DialogContent,
} from "@/shared/components/ui/dialog";
import { CreateProductComponent } from "../components/CreateProductComponent";
import { Description, DialogTitle } from '@radix-ui/react-dialog';

export default function ProductsListPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const isCreateModalOpen = location.pathname === '/products/create';
    const isEditModalOpen = location.pathname.startsWith('/products/edit/');
    const isModalOpen = isCreateModalOpen || isEditModalOpen;

    const {
        isFiltersOpen,
        openFilters,
        closeFilters,
    } = useProductsFilter();


    const handleModalChange = (open) => {
        if (!open) {
            navigate("/products");
        }
    };

    return (
        <section className='flex flex-col gap-6'>
            <h3 className='text-3xl font-semibold self-start'>Inventario de productos</h3>
            <div className="w-full h-full">
                <ProductsFiltersBar
                    onToggleFilters={openFilters}
                />
                <ProductsTable />

                <ProductsFiltersPopup
                    open={isFiltersOpen}
                    onClose={closeFilters}
                />

                <Dialog open={isModalOpen} onOpenChange={handleModalChange}>
                    <DialogTitle className="sr-only">
                        {isEditModalOpen ? "Editar Producto" : "Registrar Producto"}
                    </DialogTitle>
                    <Description className="sr-only">
                        {isEditModalOpen
                            ? "Formulario para editar producto existente"
                            : "Formulario para registrar un nuevo producto"
                        }
                    </Description>
                    <DialogContent className="max-h-[90vh] overflow-y-auto p-0 gap-0 max-w-2xl bg-stokia-neutral-50 border-0">
                        <div className="overflow-y-auto">
                            <CreateProductComponent />
                        </div>
                    </DialogContent>
                </Dialog>
            </div>
        </section>
    );
}