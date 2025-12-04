import { createContext, useContext, useState, useEffect, useRef } from "react";
import { getProducts } from "../services/productService";
import { productMapper } from "../mappers/productMap";
import INITIAL_PAGINATION_STATE from "@/lib/constants/initialPagination";
import mapPaginationResponse from "../mappers/productPagination";

const ProductsContext = createContext(undefined);

const INITIAL_FILTERS_STATE = {
  sort: undefined,
  q: undefined,
  categoryId: undefined,
  lowStock: undefined,
  includeInactive: undefined,
  deleted: undefined,
  name: undefined,
  category: undefined,
};

const calculateStockFilterValue = (lowStock) => {
  return lowStock === true ? 'low' : lowStock === false ? 'normal' : 'all';
};

const calculateActiveFiltersCount = (currentFilters) => {
  let count = 0;
  for (const key in currentFilters) {
    if (key === 'sort') continue;

    const value = currentFilters[key];
    if (value !== undefined && value !== null && value !== '') {
      if (typeof value === 'boolean') {
        count++;
      } else if (typeof value === 'string' || typeof value === 'number') {
        count++;
      }
    }
  }
  return count;
};


export function ProductsProvider({ children }) {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [pagination, setPagination] = useState(INITIAL_PAGINATION_STATE);
  const [filters, setFilters] = useState(INITIAL_FILTERS_STATE);

  const searchTimeoutRef = useRef(null);

  const fetchProducts = async (page = 0, size = 20, currentFilters = filters) => {
    setLoading(true);
    setError(null);

    try {
      const response = await getProducts({
        page,
        size,
        ...currentFilters
      });

      if (response && response.data) {
        const list = response.data.content || [];
        setProducts(list.map(productMapper));
        setPagination(mapPaginationResponse(response.data));
      }
    } catch (e) {
      console.error("Error fetching products:", e);
      setError(e.message || "Error al cargar productos");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts(0, 20, filters);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (searchTimeoutRef.current) {
      clearTimeout(searchTimeoutRef.current);
    }

    searchTimeoutRef.current = setTimeout(() => {
      fetchProducts(0, pagination.pageSize, filters);
    }, 250);

    return () => clearTimeout(searchTimeoutRef.current);
  }, [filters.q]);

  const goToPage = (page) => {
    if (page >= 0 && page < pagination.totalPages) {
      fetchProducts(page, pagination.pageSize, filters);
    }
  };

  const nextPage = () => {
    if (!pagination.isLast) {
      fetchProducts(pagination.currentPage + 1, pagination.pageSize, filters);
    }
  };

  const previousPage = () => {
    if (!pagination.isFirst) {
      fetchProducts(pagination.currentPage - 1, pagination.pageSize, filters);
    }
  };

  const changePageSize = (newSize) => {
    fetchProducts(0, newSize, filters);
  };

  const updateFilters = (newFilters) => {
    const updatedFilters = { ...filters, ...newFilters };
    setFilters(updatedFilters);
    fetchProducts(0, pagination.pageSize, updatedFilters);
  };

  const clearFilters = () => {
    setFilters(INITIAL_FILTERS_STATE);
    fetchProducts(0, pagination.pageSize, INITIAL_FILTERS_STATE);
  };

  const setSearch = (searchTerm) => {
    setFilters(prev => ({ ...prev, q: searchTerm }));
  };

  const setCategory = (categoryId) => {
    updateFilters({ categoryId });
  };

  const setSort = (sortValue) => {
    updateFilters({ sort: sortValue });
  };

  const setLowStock = (lowStock) => {
    updateFilters({ lowStock });
  };

  const setStockFilter = (value) => {
    if (value === 'all') {
      updateFilters({ lowStock: undefined });
    } else if (value === 'low') {
      updateFilters({ lowStock: true });
    } else {
      updateFilters({ lowStock: false });
    }
  };

  const addProduct = (product) => {
    const mapped = productMapper(product);
    setProducts([mapped, ...products]);
    setPagination(prev => ({
      ...prev,
      totalElements: prev.totalElements + 1
    }));
  };

  const setDeleted = (deleted) => {
    if (deleted === 'all') {
      updateFilters({ deleted: undefined });
    } else if (deleted === 'true') {
      updateFilters({ deleted: true });
    } else {
      updateFilters({ deleted: false });
    }
  };

  const updateProduct = (id, updatedProduct) => {
    setProducts((prev) =>
      prev.map((p) => (p.id === id ? productMapper(updatedProduct) : p))
    );
  };

  const removeProduct = (id) => {
    setProducts((prev) => prev.filter((p) => p.id !== id));
    setPagination(prev => ({
      ...prev,
      totalElements: Math.max(0, prev.totalElements - 1)
    }));
  };

  const refreshProducts = () => {
    fetchProducts(pagination.currentPage, pagination.pageSize, filters);
  };

  const stockFilterValue = calculateStockFilterValue(filters.lowStock);

  const activeFiltersCount = calculateActiveFiltersCount(filters);

  const value = {
    products,
    loading,
    error,
    pagination,
    filters,
    stockFilterValue,
    activeFiltersCount,
    addProduct,
    updateProduct,
    removeProduct,
    goToPage,
    nextPage,
    previousPage,
    changePageSize,
    refreshProducts,
    updateFilters,
    clearFilters,
    setSearch,
    setCategory,
    setSort,
    setLowStock,
    setStockFilter,
    setDeleted,
  };

  return (
    <ProductsContext.Provider value={value}>
      {children}
    </ProductsContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useProducts() {
  const context = useContext(ProductsContext);
  if (context === undefined) {
    throw new Error("useProducts debe usarse dentro de un ProductsProvider");
  }
  return context;
}