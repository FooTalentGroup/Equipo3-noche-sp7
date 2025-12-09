import React, { useState } from "react";
import { OrderTabs } from "../components/OrderTabs";
import { Button } from "@/shared/components/ui/button";
import { CustomerSection } from "../components/CustomerSection";
import { ProductsSection } from "../components/ProductsSection";
import { ProductCard } from "../components/CardResult";
import { SalesSummary } from "../components/SalesSummary";
import { useSalesProductSearch } from "../hooks/useSalesProductSearch";
import { useCart } from "../hooks/useCart"; 

const SalesPage = () => {
  const [activeTab, setActiveTab] = useState("register");

  const { 
    items, 
    addItem, 
    subtotal, 
    total, 
    removeItem, 
    decrementItemQuantity 
  } = useCart(); 

  const {
    searchQuery: productQuery,
    setSearchQuery: setProductQuery,
    products,
    loading: loadingProducts,
  } = useSalesProductSearch();

  const handleClearSearch = () => {
    setProductQuery("");
  };

  const handleSelectProduct = (product) => {
    addItem(product);
  };

  return (
    <div className="min-h-screen w-full flex flex-col">
      <OrderTabs activeTab={activeTab} onTabChange={setActiveTab} />

      <div className="bg-background rounded-2xl shadow-sm p-8 border border-border max-w-[1200px] mx-auto w-full">
        <CustomerSection />

        <ProductsSection
          productQuery={productQuery}
          setProductQuery={setProductQuery}
          handleClearSearch={handleClearSearch}
        />

        <div className="flex-grow grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-8 items-start">
          <section className="flex-grow flex flex-col">
            <h2 className="text-base font-medium text-foreground mb-4">
              Resultados
            </h2>

            <div className="overflow-y-auto max-h-[450px] bg-secondary border border-border rounded-lg p-4 custom-scroll">
              {loadingProducts && (
                <div className="text-center p-8 text-muted-foreground">
                  Cargando productos...
                </div>
              )}

              {!loadingProducts && productQuery && products.length === 0 && (
                <div className="text-center p-8 text-muted-foreground">
                  No se encontraron productos para "{productQuery}"
                </div>
              )}

              {products.length > 0 && (
                <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-6 min-w-[773px]">
                  {products.map((product) => (
                    <ProductCard
                      key={product.id || product.name}
                      id={product.id} 
                      name={product.name}
                      stock={product.currentStock}
                      price={product.price}
                      imageUrl={product.photoUrl}
                      onAddToCart={handleSelectProduct} 
                    />
                  ))}
                </div>
              )}
            </div>
          </section>

          <div className="sticky top-8 flex flex-col space-y-4">
            <h2 className="text-base font-medium text-foreground mb-4">
              Resumen de compra
            </h2>
            <SalesSummary 
                cartItems={items} 
                subtotal={subtotal} 
                total={total}
                onAddItem={addItem}
                onDecrementItem={decrementItemQuantity}
                onRemoveItem={removeItem}
            />
            <div className="flex space-x-4">
              <Button
                onClick={() => console.log("Agregar nota")}
                className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[131px] h-[40px]"
              >
                <span>Agregar nota</span>
              </Button>
              <Button
                onClick={() => console.log("Finalizar pedido")}
                className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[149px] h-[40px]"
              >
                <span>Finalizar pedido</span>
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SalesPage;