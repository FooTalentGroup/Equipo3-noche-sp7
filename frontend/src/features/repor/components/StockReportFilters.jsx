import { Label } from "@/shared/components/ui/label";
import { Button } from "@/shared/components/ui/button";
import { Calendar1, X } from "lucide-react";
import { useState, useRef, useEffect } from "react";
import { SearchIcon } from "lucide-react";
import { useProductSearch } from "../hooks/useProductSearch";

export function StockReportFilters({
  productName,
  onProductChange,
  startDate,
  endDate,
  onOpenDateModal,
  formatDateArg,
}) {
  const [isOpen, setIsOpen] = useState(false);
  const {
    query,
    setQuery,
    products,
    isLoading,
    selectedProduct,
    setSelectedProduct,
  } = useProductSearch();

  const dropdownRef = useRef(null);

  useEffect(() => {
    if (productName && productName !== query) {
      setQuery(productName);
    }
  }, [productName]);

  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleProductSelect = (product) => {
    setSelectedProduct(product);
    setQuery(product.name);
    onProductChange(product.name);
    setIsOpen(false);
  };

  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    setIsOpen(true);
    if (!value) {
      setSelectedProduct(null);
      onProductChange("");
    }
  };

  const clearSelection = () => {
    setQuery("");
    setSelectedProduct(null);
    onProductChange("");
  };

  return (
    <>
      <Label className="flex flex-col gap-3 items-start">
        Producto
        <div className="relative w-full" ref={dropdownRef}>
          <div className="relative">
            <input
              type="text"
              value={query}
              onChange={handleInputChange}
              onFocus={() => setIsOpen(true)}
              placeholder="Buscar producto"
              className="w-full px-4 py-2 pr-20 border border-stokia-neutral-300 rounded-md focus:outline-none focus:ring-2 focus:ring-stokia-primary-600"
            />
            <div className="absolute right-3 top-1/2 -translate-y-1/2 flex items-center gap-2">
              {query && (
                <button
                  onClick={clearSelection}
                  className="hover:bg-stokia-neutral-200 rounded p-0.5"
                >
                  <X className="h-4 w-4 text-stokia-neutral-500" />
                </button>
              )}
              <SearchIcon className="h-4 w-4 text-stokia-neutral-500" />
            </div>
          </div>

          {isOpen && query && products.length > 0 && (
            <div className="absolute z-50 w-full mt-1 bg-white border border-stokia-neutral-200 rounded-md shadow-lg max-h-60 overflow-auto">
              {products.map((product) => (
                <button
                  key={product.id}
                  onClick={() => handleProductSelect(product)}
                  className="w-full px-4 py-2 text-left hover:bg-stokia-neutral-100 flex flex-col"
                >
                  <span className="font-medium text-sm">{product.name}</span>
                  <span className="text-xs text-stokia-neutral-500">
                    {product.category?.name}
                  </span>
                </button>
              ))}
            </div>
          )}

          {isLoading && (
            <div className="absolute z-50 w-full mt-1 bg-white border border-stokia-neutral-200 rounded-md shadow-lg p-4 text-center text-sm text-stokia-neutral-500">
              Buscando...
            </div>
          )}
        </div>
      </Label>

      <Label className="flex flex-col gap-3 items-start">
        Selección de período
        <Button variant="stokia" className="" onClick={onOpenDateModal}>
          <Calendar1 size={12} />
          {startDate && endDate
            ? `${formatDateArg(startDate)} - ${formatDateArg(endDate)}`
            : "Seleccionar período"}
        </Button>
      </Label>
    </>
  );
}
