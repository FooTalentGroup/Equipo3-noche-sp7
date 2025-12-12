import { useState, useRef, useEffect } from 'react';
import { Label } from "@/shared/components/ui/label";
import { NativeSelect } from "@/shared/components/ui/native-select";
import { SearchIcon } from "lucide-react";
import { useProductSearch } from '../hooks/useProductSearch';

const currentYear = new Date().getFullYear();
const YEARS = Array.from({ length: 5 }, (_, i) => currentYear - i);

export function CostReportFilters({ year, onYearChange, productName, onProductChange }) {
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
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        }

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
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
            onProductChange('');
        }
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
                            className="w-full px-4 py-2 pr-10 border border-stokia-neutral-300 rounded-md focus:outline-none focus:ring-2 focus:ring-stokia-primary-600"
                        />
                        <SearchIcon className="absolute right-3 top-1/2 -translate-y-1/2 h-4 w-4 text-stokia-neutral-500" />
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
                                    <span className="text-xs text-stokia-neutral-500">{product.category?.name}</span>
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
                Año
                <NativeSelect value={year} onChange={(e) => onYearChange(Number(e.target.value))}>
                    <option value="">Seleccionar</option>
                    {YEARS.map((y) => (
                        <option key={y} value={y}>
                            {y}
                        </option>
                    ))}
                </NativeSelect>
            </Label>
        </>
    );
}
