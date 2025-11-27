import { useState, useMemo, useEffect } from "react";
import { Edit, Trash2, ChevronLeft, ChevronRight, Loader, LoaderCircle } from "lucide-react";
import { Button } from "@/shared/components/ui/button.jsx";
import Badge from "@/features/products/components/Badge.jsx";
import { useProducts } from "../context/ProductsContext";
import { useNavigate } from "react-router";
import { ConfirmDialog } from "./ConfirmDialog";
import { useDeleteProduct } from "../hooks/useDeleteProduct";
import { Trash } from "lucide-react";
import ActionsMenu from './ActionsMenu';

const parsePrice = (val) => {
  if (val == null) return 0;
  if (typeof val === "number") return val;
  const num = String(val).replace(/[^0-9.-]+/g, "");
  const parsed = Number(num);
  return Number.isFinite(parsed) ? parsed : 0;
};

const normalize = (s) =>
  String(s || "")
    .normalize?.("NFD")
    .replace(/\p{Diacritic}/gu, "")
    .toLowerCase();

// ====================================================

export const ProductsTable = ({
  searchQuery = "",
  filters = {},
  sort = "name_asc",
}) => {
  // const [products, setProducts] = useState(generateRandomProducts());
  const [currentPage, setCurrentPage] = useState(1);
  const [isOpenDelete, setIsOpenDelete] = useState(false);
  const [selectedProductId, setS] = useState(null);
  const itemsPerPage = 10;
  const { products } = useProducts();
  const { handleDelete: handleDeleteProduct } = useDeleteProduct();
  const navigate = useNavigate();

  const handleOpenDelete = (id) => {
    setS(id);
    setIsOpenDelete(true);
  };

  // eslint-disable-next-line no-unused-vars
  const getStockBadge = (actual, minimo) => {
    if (actual === 0) {
      return <Badge variant="destructive">Sin stock</Badge>;
    }
    if (actual <= 10) {
      return <Badge variant="warning">Bajo stock</Badge>;
    }
    return <Badge variant="success">Alto stock</Badge>;
  };

    const filteredProducts = useMemo(() => {
        let result = [...(products || [])];

    if (searchQuery) {
      const term = searchQuery.toLowerCase();
      result = result.filter(
        (p) =>
          p.name.toLowerCase().includes(term) ||
          p.category.toLowerCase().includes(term)
      );
    }

    if (filters.category && filters.category !== "all") {
      const catNorm = normalize(filters.category);
      result = result.filter((p) => normalize(p.category) === catNorm);
    }

    if (filters.stockStatus === "in")
      result = result.filter((p) => p.stock_actual > 0);
    if (filters.stockStatus === "out")
      result = result.filter((p) => p.stock_actual === 0);

    switch (sort) {
      case "name_asc":
        result.sort((a, b) => a.name.localeCompare(b.name));
        break;
      case "name_desc":
        result.sort((a, b) => b.name.localeCompare(a.name));
        break;
      case "price_asc":
        result.sort((a, b) => parsePrice(a.price) - parsePrice(b.price));
        break;
      case "price_desc":
        result.sort((a, b) => parsePrice(b.price) - parsePrice(a.price));
        break;
    }

    return result;
  }, [products, searchQuery, filters, sort]);

  useEffect(() => {
    setCurrentPage(1);
  }, [searchQuery, filters, sort]);

  const totalPages = Math.ceil(filteredProducts.length / itemsPerPage);
  useEffect(() => {
    if (totalPages === 0) {
      setCurrentPage(1);
    } else if (currentPage > totalPages) {
      setCurrentPage(1);
    }
  }, [totalPages, currentPage]);

    const paginated = filteredProducts.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

    const isLoading = !products || products.length === 0;

    if (isLoading) {
        return (
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] max-h-[673px] flex items-center justify-center">
                <div className="flex flex-col items-center justify-center gap-3 my-4">
                    <LoaderCircle className="h-8 w-8 text-slate-600 animate-spin" />
                    <span className="text-sm text-gray-600">Cargando productos...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] max-h-[673px]">
            <div className="overflow-x-auto">
                <table className="w-full text-sm text-left">
                    <thead className="text-[14px] bg-slate-200 text-[#404040] font-normal h-[46px]">
                        <tr>
                            <th className="px-6 py-3 text-[#404040]">Estado</th>
                            <th className="px-6 py-3 text-[#404040]">Producto</th>
                            <th className="px-6 py-3 text-[#404040]">Categoría</th>
                            <th className="px-6 py-3 text-[#404040]">Precio</th>
                            <th className="px-6 py-3 text-[#404040]">Descuento</th>
                            <th className="px-6 py-3 text-[#404040] text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                        {paginated.map(product => (
                            <tr key={product.id} className="hover:bg-gray-50 transition-colors">
                                <td className="px-6 py-4">
                                    <div className="w-28">
                                        {getStockBadge(product.stock_actual, product.min_stock)}
                                    </div>
                                </td>
                                <td className="px-6 py-4 font-normal text-[#171717] text-[14px]">{product.name}</td>
                                <td className="px-6 py-4 text-[#525252] font-normal text-[14px]">{product.category}</td>
                                <td className="px-6 py-4 font-normal text-[14px] text-[#171717]">${product.price}</td>
                                <td className="px-6 py-4">
                                    {product.descuento > 0 ? (
                                        <span className="font-normal text-[14px] text-[#525252]">{product.descuento}%</span>
                                    ) : (
                                        <span className="font-normal text-[14px] text-[#525252]">0%</span>
                                    )}
                                </td>
                                <td className="px-6 py-4">
                                    <div className="flex items-center justify-center gap-4">
                                        <ActionsMenu handleEdit={() => navigate(`/products/edit/${product.id}`)} handleDelete={() => handleOpenDelete(product.id)} />
                                        <button onClick={() => handleOpenDelete(product.id)} className="text-destructive cursor-pointer transition">
                                            <Trash2 className="w-4 h-4" />
                                        </button>

                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

      <div className="flex items-center justify-center px-6 py-3 bg-gray-50 border-t">
        <div className="flex items-center gap-2">
          <button
            onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
            disabled={currentPage === 1}
            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
          >
            <ChevronLeft className="w-4 h-4" /> Previous
          </button>

          <div className="flex gap-1 justify-center">
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
              <button
                key={page}
                onClick={() => setCurrentPage(page)}
                className={`px-3 py-1.5 text-sm rounded-md ${
                  currentPage === page
                    ? "bg-slate-600 text-white"
                    : "text-gray-700 hover:bg-gray-200"
                }`}
              >
                {page}
              </button>
            ))}
          </div>

          <button
            onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
            disabled={currentPage === totalPages || totalPages === 0}
            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
          >
            Next <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>
      <ConfirmDialog
        isOpen={isOpenDelete}
        handleOpenChange={setIsOpenDelete}
        acceptTitle="Eliminar"
        onCancel={() => setIsOpenDelete(false)}
        dialogTitle="Borrar producto"
        dialogDescription="¿Está seguro que desea eliminar este producto?"
        variant="destructive"
        icon={Trash}
        onAccept={() => handleDeleteProduct(selectedProductId)}
      />
    </div>
  );
};
