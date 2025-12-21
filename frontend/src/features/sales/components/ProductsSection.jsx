import { ProductsSearchInput } from "./ProductsSearchInput";

export function ProductsSection({
  productQuery,
  setProductQuery,
  handleClearSearch,
}) {
  return (
    <section className="mb-6">
      <h2 className="text-base font-medium text-foreground mb-3">
        Buscar producto
      </h2>
      <div className="flex flex-col gap-4">
        <div className="relative">
          <ProductsSearchInput
            value={productQuery}
            onChange={setProductQuery}
            onClear={handleClearSearch}
            placeholder="Buscar productos por nombre o código"
            className="w-432 h-9"
          />
        </div>
      </div>
    </section>
  );
}
