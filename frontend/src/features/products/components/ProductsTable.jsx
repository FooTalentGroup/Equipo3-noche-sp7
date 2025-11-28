import { useState, useEffect, useMemo } from 'react';
import { Trash2, ChevronLeft, ChevronRight, LoaderCircle } from 'lucide-react';
import Badge from "@/features/products/components/Badge.jsx";
import { useProducts } from '../context/ProductsContext';
import ActionsMenu from './ActionsMenu';

export const ProductsTable = ({ searchQuery = '', filters = {}, sort = 'name_asc' }) => {
    const [currentPage, setCurrentPage] = useState(0);
    const { products, isLoading, pagination, fetchProducts, deleteProduct } = useProducts();

    const handleDelete = (id) => deleteProduct(id);

    const getStockBadge = (actual, minimo) => {
        if (actual === 0) return <Badge variant="destructive">Sin stock</Badge>;
        if (actual <= 10) return <Badge variant="warning">Bajo stock</Badge>;
        return <Badge variant="success">Alto stock</Badge>;
    };

    // Convert frontend sort to backend sort format
    const getBackendSort = (sortValue) => {
        switch (sortValue) {
            case 'name_asc':
                return 'name,asc';
            case 'name_desc':
                return 'name,desc';
            case 'price_asc':
                return 'price,asc';
            case 'price_desc':
                return 'price,desc';
            default:
                return 'name,asc';
        }
    };

    // Fetch products when filters/search/sort/page change
    useEffect(() => {
        const params = {
            page: currentPage,
            size: 10,
            sort: getBackendSort(sort),
            q: searchQuery || undefined,
            lowStock: filters.stockStatus === 'low' ? true : undefined,
            categoryId: filters.category && filters.category !== 'all' ? filters.category : undefined,
        };
        fetchProducts(params);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage, searchQuery, filters, sort]);

    // Reset to page 0 when filters/search/sort change
    useEffect(() => {
        setCurrentPage(0);
    }, [searchQuery, filters, sort]);

    const totalPages = pagination.totalPages || 1;

    // -------------------------
    // LOADING STATE
    // -------------------------
    if (isLoading) {
        return (
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] min-h-[300px] flex items-center justify-center">
                <div className="flex flex-col items-center justify-center gap-3 my-4">
                    <LoaderCircle className="h-8 w-8 text-slate-600 animate-spin" />
                    <span className="text-sm text-gray-600">Cargando productos...</span>
                </div>
            </div>
        );
    }

    // -------------------------
    // MAIN UI
    // -------------------------
    return (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 max-w-[1086px]">

            {/* Scrollable table only */}
            <div className="max-h-[620px] overflow-y-auto">
                <table className="w-full text-sm text-left">
                    <thead className="text-[14px] bg-slate-200 text-[#404040] font-normal h-[46px] sticky top-0 z-10">
                        <tr>
                            <th className="px-6 py-3">Estado</th>
                            <th className="px-6 py-3">Producto</th>
                            <th className="px-6 py-3">Categoría</th>
                            <th className="px-6 py-3">Precio</th>
                            <th className="px-6 py-3">Descuento</th>
                            <th className="px-6 py-3 text-center">Acciones</th>
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-gray-200">
                        {products.map((product) => (
                            <tr key={product.id} className="hover:bg-gray-50 transition-colors">
                                <td className="px-6 py-4">
                                    <div className="w-28">
                                        {getStockBadge(product.stock_actual, product.min_stock)}
                                    </div>
                                </td>

                                <td className="px-6 py-4 text-[#171717] text-[14px]">{product.name}</td>
                                <td className="px-6 py-4 text-[#525252] text-[14px]">{product.category}</td>
                                <td className="px-6 py-4 text-[#171717] text-[14px]">${product.price}</td>

                                <td className="px-6 py-4 text-[#525252] text-[14px]">
                                    {product.descuento > 0 ? `${product.descuento}%` : "0%"}
                                </td>

                                <td className="px-6 py-4">
                                    <div className="flex items-center justify-center gap-4">
                                        <ActionsMenu product={product} />
                                        <button
                                            onClick={() => handleDelete(product.id)}
                                            className="text-red-600 cursor-pointer transition"
                                        >
                                            <Trash2 className="w-4 h-4" />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}

                        {!isLoading && products.length === 0 && (
                            <tr>
                                <td colSpan={6} className="px-6 py-6 text-center text-gray-500 text-sm">
                                    No hay productos para mostrar.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Pagination FIXED at bottom (not clipped) */}
            {totalPages > 1 && (
                <div className="flex items-center justify-center px-6 py-3 bg-gray-50 border-t">
                    <div className="flex items-center gap-2">
                        <button
                            onClick={() => setCurrentPage(p => Math.max(0, p - 1))}
                            disabled={currentPage === 0}
                            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                        >
                            <ChevronLeft className="w-4 h-4" /> Previous
                        </button>

                        <div className="flex gap-1 justify-center">
                            {Array.from({ length: totalPages }, (_, i) => i).map((page) => (
                                <button
                                    key={page}
                                    onClick={() => setCurrentPage(page)}
                                    className={`px-3 py-1.5 text-sm rounded-md ${currentPage === page
                                            ? "bg-slate-600 text-white"
                                            : "text-gray-700 hover:bg-gray-200"
                                        }`}
                                >
                                    {page + 1}
                                </button>
                            ))}
                        </div>

                        <button
                            onClick={() => setCurrentPage(p => Math.min(totalPages - 1, p + 1))}
                            disabled={currentPage >= totalPages - 1 || totalPages === 0}
                            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                        >
                            Next <ChevronRight className="w-4 h-4" />
                        </button>
                    </div>
                </div>
            )}

        </div>
    );
};
