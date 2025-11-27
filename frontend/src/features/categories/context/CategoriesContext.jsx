import { createContext, useContext } from "react";
import { useApiQueryFn } from "@/shared/hooks/useApi";
import { getCategories } from "../services/categoriesService";

const CategoriesContext = createContext(undefined);

export function CategoriesProvider({ children }) {
  const { data, isFetching, isLoading, error } = useApiQueryFn(
    ["categories"], 
    getCategories
  );

  const categories = data ?? [];

  const getCategoryById = (id) => {
    return categories.find((c) => c.id === id);
  };

  const getCategoryByName = (name) => {
    return categories.find((c) => c.name.toLowerCase() === name.toLowerCase());
  };

  const value = {
    categories,
    isFetching,
    isLoading,
    error,
    getCategoryById,
    getCategoryByName,
  };

  return (
    <CategoriesContext.Provider value={value}>
      {children}
    </CategoriesContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useCategories() {
  const context = useContext(CategoriesContext);
  if (context === undefined) {
    throw new Error(
      "useCategories debe usarse dentro de un CategoriesProvider"
    );
  }
  return context;
}