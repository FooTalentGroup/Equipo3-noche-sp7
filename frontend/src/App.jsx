import { RouterProvider } from "react-router-dom";
import router from "@/infrastructure/router/routes.config.jsx";
import { Toaster } from "@/shared/components/ui/sonner.jsx";
import { CategoriesProvider } from "./features/categories/context/CategoriesContext";
import { ProductsProvider } from "./features/products/context/ProductsContext";

export default function App() {
  return (
    <>
      <CategoriesProvider>
        <ProductsProvider>
          <RouterProvider router={router} />
        </ProductsProvider>
      </CategoriesProvider>
      <Toaster />
    </>
  );
}
