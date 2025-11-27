import { createContext, useContext, useState, useEffect } from "react";
import { getProducts } from "../services/productService";
import { productMapper } from "../mappers/productMap";

// =============== CONTEXT ===============
const ProductsContext = createContext(undefined);

export function ProductsProvider({ children }) {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getProducts();
        const list = Array.isArray(response?.data) ? response.data : [];
        setProducts(list.map((product) => productMapper(product)));
      } catch (e) {
        console.error("Error fetching products:", e);
      }
    }
    fetchData();
  }, []);

  const addProduct = (product) => {
    setProducts([...products, productMapper(product)]);
  };

  const updateProduct = (id, updatedProduct) => {
    setProducts((prev) =>
      prev.map((p) => (p.id === id ? productMapper(updatedProduct) : p))
    );
  };

  const removeProduct = (id) => {
    setProducts((prev) => prev.filter((p) => p.id !== id));
  };

  const value = {
    products,
    addProduct,
    updateProduct,
    removeProduct,
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
