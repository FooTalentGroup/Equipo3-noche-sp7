import { useState } from "react";
import { Trash2, ChevronLeft, ChevronRight, LoaderCircle } from "lucide-react";
import Badge from "@/features/products/components/Badge.jsx";
import { useProducts } from "../context/ProductsContext";
import { useNavigate } from "react-router";
import { ConfirmDialog } from "./ConfirmDialog";
import { useDeleteProduct } from "../hooks/useDeleteProduct";
import { Trash } from "lucide-react";
import ActionsMenu from './ActionsMenu';
import { Button } from "@/shared/components/ui/button";

export const ProductsTable = () => {
  const [isOpenDelete, setIsOpenDelete] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState(null);

  const {
    products,
    loading,
    pagination,
    nextPage,
    previousPage,
    goToPage
  } = useProducts();

  const { handleDelete: handleDeleteProduct } = useDeleteProduct();
  const navigate = useNavigate();

  const handleOpenDelete = (id) => {
    setSelectedProductId(id);
    setIsOpenDelete(true);
  };

  const getStockBadge = (actual, minimo) => {
    const stock = actual ?? 0;
    if (stock === 0) {
      return <Badge title="Sin stock" variant="destructive">Sin stock</Badge>;
    }
    if (stock <= minimo) {
      return <Badge title={`${actual}/${minimo}`} variant="warning">Bajo stock</Badge>;
    }
    return <Badge title={`${actual}/${minimo}`} variant="success">Alto stock</Badge>;
  };

  return (
    <div className="h-full max-w-5xl max-h-[720px] flex flex-col shadow-lg">
      <div className={`relative overflow-x-auto ${loading ? 'h-[400px] overflow-hidden' : ''} flex-1`}>

        <table className="w-full text-left border-separate border-spacing-0">
          <thead className="text-sm bg-stokia-primary-100 text-stokia-neutral-950 font-normal h-[46px] sticky top-0 z-10">
            <tr>
              <th className="px-6 py-3 rounded-tl-xl">Estado</th>
              <th className="px-6 py-3">Producto</th>
              <th className="px-6 py-3">Categoría</th>
              <th className="px-6 py-3">Precio</th>
              <th className="px-6 py-3">Descuento</th>
              <th className="px-6 py-3 rounded-tr-xl">Acciones</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-stokia-neutral-100">
            {products.map((product) => (
              <tr key={product.id} className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm">
                <td className="px-6 py-4">
                  <div className="w-28">
                    {getStockBadge(product.currentStock, product.minStock)}
                  </div>
                </td>

                <td className="px-6 py-4">{product.name}</td>

                <td className="px-6 py-4">
                  {typeof product.category === "string"
                    ? product.category
                    : product.categoryObj?.name || product.category || "Sin categoría"}
                </td>

                <td className="px-6 py-4">${product.price}</td>

                <td className="px-6 py-4">
                  {product.descuento > 0 ? `${product.descuento}%` : "0%"}
                </td>

                <td className="px-6 py-4">
                  <div className="flex items-center justify-center gap-4">
                    <ActionsMenu
                      product={product}
                      handleEdit={() => navigate(`/products/edit/${product.id}`)}
                      handleDelete={() => handleOpenDelete(product.id)}
                    />

                    <button
                      onClick={() => handleOpenDelete(product.id)}
                      className="text-destructive cursor-pointer transition"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {loading && (
          <div className="flex flex-col items-center justify-center absolute inset-0 bg-stokia-neutral-50/90 z-20">
            <LoaderCircle className="h-16 w-16 text-stokia-primary-600 animate-spin" />
            <span className="text-sm text-stokia-primary-600 mt-2">
              Cargando productos...
            </span>
          </div>
        )}

        {products.length === 0 && !loading && (
          <div className="absolute inset-0 flex items-center justify-center">
            <span className="px-6 py-6 text-center text-stokia-neutral-600 text-sm">
              No hay productos para mostrar.
            </span>
          </div>
        )}
      </div>

      {pagination.totalPages > 1 && (
        <div className="flex items-center justify-center px-6 py-3 bg-stokia-neutral-50 border-t mt-auto">
          <div className="flex items-center gap-2">
            <Button
              onClick={previousPage}
              disabled={pagination.isFirst}
              variant="outline"
            >
              <ChevronLeft className="w-4 h-4" /> Anterior
            </Button>
            <div className="flex gap-1 justify-center">
              {Array.from({ length: pagination.totalPages }, (_, i) => i).map(
                (page) => (
                  <Button
                    key={page}
                    onClick={() => goToPage(page)}
                    size="sm"
                    variant={page === pagination.currentPage ? "stokia" : "outline"}
                  >
                    {page + 1}
                  </Button>
                )
              )}
            </div>

            <Button
              onClick={nextPage}
              disabled={pagination.isLast}
              variant="outline"
            >
              Siguiente <ChevronRight className="w-4 h-4" />
            </Button>
          </div>
        </div>
      )}

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
