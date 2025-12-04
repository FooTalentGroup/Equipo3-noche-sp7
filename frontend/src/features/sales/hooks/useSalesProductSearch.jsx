import { useState, useEffect, useMemo } from "react";
import { getProducts } from "@/features/products/services/productService";
import { useDebounce } from "use-debounce";

export const useSalesProductSearch = (debounceTime = 300) => {
  const [searchQuery, setSearchQuery] = useState("");
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);

  const [debouncedQuery] = useDebounce(searchQuery, debounceTime);

  useEffect(() => {
    if (debouncedQuery.trim() === "") {
      setProducts([]);
      return;
    }

    const fetchProducts = async () => {
      setLoading(true);
      try {
        const response = await getProducts({
          q: debouncedQuery,
          size: 10,
        });

        const productList = response.data?.content || [];
        
        setProducts(productList);
      } catch (error) {
        console.error("Error al buscar productos en la venta:", error);
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [debouncedQuery]);

  const showResults = useMemo(() => {
    return searchQuery.trim() !== "" && (loading || products.length > 0);
  }, [searchQuery, loading, products]);

  return {
    searchQuery,
    setSearchQuery,
    products,
    loading,
    showResults,
  };
};