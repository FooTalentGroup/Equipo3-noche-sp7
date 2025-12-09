import { useState } from "react";
import { Trash2, LoaderCircle } from "lucide-react";
import Badge from "@/features/products/components/Badge.jsx";
import { useProducts } from "../context/ProductsContext";
import { useNavigate } from "react-router";
import { ConfirmDialog } from "./ConfirmDialog";
import { useDeleteProduct } from "../hooks/useDeleteProduct";
import { Trash } from "lucide-react";
import ActionsMenu from './ActionsMenu';
import { Pagination } from "@/shared/components/ui/pagination";

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

  return (
    <div className="h-full max-w-5xl max-h-[720px] flex flex-col shadow-lg">
      <div className={`relative overflow-x-auto ${loading ? 'h-[400px] overflow-hidden' : ''} flex-1`}>
        <table className="w-full text-left border-separate border-spacing-0">
          <thead className="text-sm bg-stokia-primary-100 text-stokia-neutral-950 font-normal h-[46px] sticky top-0 z-10">
            <tr className="[&_th]:px-6 [&_th]:py-3">
              <th className="rounded-tl-xl">Estado</th>
              <th>Producto</th>
              <th>Categoría</th>
              <th>Precio</th>
              <th>Descuento</th>
              <th className="rounded-tr-xl">Acciones</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-stokia-neutral-100">
            {products.map((product) => (
              <tr key={product.id} className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm [&_td]:px-6 [&_td]:py-4">
                <td>
                  <div className="w-28">
                    {getStockBadge(product.currentStock, product.minStock)}
                  </div>
                </td>

                <td>{product.name}</td>

                <td>
                  {typeof product.category === "string"
                    ? product.category
                    : product.categoryObj?.name || product.category || "Sin categoría"}
                </td>

                <td>${product.price}</td>

                <td>
                  {product.descuento > 0 ? `${product.descuento}%` : "0%"}
                </td>

                <td>
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
        <Pagination
          currentPage={pagination.currentPage}
          totalPages={pagination.totalPages}
          onPageChange={goToPage}
          onNext={nextPage}
          onPrevious={previousPage}
          className="py-3"
        />
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
