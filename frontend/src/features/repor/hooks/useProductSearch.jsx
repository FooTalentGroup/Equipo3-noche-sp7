import { useState, useCallback, useEffect } from "react";
import { searchProducts } from "../services/productsReportService";

export function useProductSearch() {
  const [query, setQuery] = useState("");
  const [products, setProducts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);

  const search = useCallback(async (searchQuery) => {
    if (!searchQuery || searchQuery.trim().length < 2) {
      setProducts([]);
      return;
    }

    setIsLoading(true);

    try {
      const response = await searchProducts(searchQuery);
      setProducts(response?.data?.content || []);
    } catch (err) {
      console.error("Error searching products:", err);
      setProducts([]);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      if (query) {
        search(query);
      } else {
        setProducts([]);
      }
    }, 300);

    return () => clearTimeout(timer);
  }, [query, search]);

  return {
    query,
    setQuery,
    products,
    isLoading,
    selectedProduct,
    setSelectedProduct,
  };
}
