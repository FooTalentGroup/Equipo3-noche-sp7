import { useState, useEffect, useMemo } from "react";
import { getProducts } from "@/features/products/services/productService";
import { useDebounce } from "use-debounce";

const INITIAL_PRODUCT_COUNT = 6;

const normalizeString = (str) => {
  if (!str) return "";
  return str
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "");
};

export const useSalesProductSearch = (debounceTime = 300) => {
  const [searchQuery, setSearchQuery] = useState("");
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);

  const [initialProducts, setInitialProducts] = useState([]);

  const [debouncedQuery] = useDebounce(searchQuery, debounceTime);

  useEffect(() => {
    const loadInitialProducts = async () => {
      setLoading(true);
      try {
        const response = await getProducts({
          q: "",
          size: INITIAL_PRODUCT_COUNT,
        });

        const productList = response.data?.content || [];
        setInitialProducts(productList);
      } catch (error) {
        // Silent fail
      } finally {
        setLoading(false);
      }
    };

    loadInitialProducts();
  }, []);

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
          size: 50,
        });

        let productList = response.data?.content || [];

        const normalizedQuery = normalizeString(debouncedQuery);

        if (normalizedQuery.length > 0) {
          productList = productList.filter((product) => {
            const normalizedName = normalizeString(product.name);
            return normalizedName.includes(normalizedQuery);
          });
        }
        setProducts(productList);
      } catch (error) {
        // Silent fail
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [debouncedQuery]);

  const currentProducts = useMemo(() => {
    if (searchQuery.trim() === "") {
      return initialProducts;
    }
    return products;
  }, [searchQuery, initialProducts, products]);

  const isInitialLoading = searchQuery.trim() === "" && loading;
  const isSearchLoading = searchQuery.trim() !== "" && loading;

  return {
    searchQuery,
    setSearchQuery,
    products: currentProducts,
    loading: isSearchLoading || isInitialLoading,
  };
};
