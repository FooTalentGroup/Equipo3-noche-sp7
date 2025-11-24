import React from "react";
import { Outlet } from "react-router-dom";
import { Toaster } from "@/shared/components/ui/sonner.jsx";
import { CategoriesProvider } from "./features/categories/context/CategoriesContext";
import { ProductsProvider } from "./features/products/context/ProductsContext";
import SideMenu from "@/shared/components/navigation/SideMenu.jsx";
import { TopBar } from "@/shared/components/layout/TopBar.jsx";

export default function App() {
  return (
    <>
      <CategoriesProvider>
        <ProductsProvider>
          <div className="app-wrapper mx-auto flex">
            <SideMenu />
            <div className="flex-1 min-h-screen bg-white">
              <TopBar />
              <main className="p-6">
                <Outlet />
              </main>
            </div>
          </div>
        </ProductsProvider>
      </CategoriesProvider>

      <Toaster />
    </>
  );
}
