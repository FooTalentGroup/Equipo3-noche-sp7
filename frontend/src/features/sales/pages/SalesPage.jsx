import React, { useState } from "react";
import { OrderTabs } from "../components/OrderTabs";
import { Button } from "@/shared/components/ui/button";
import { CustomerSection } from "../components/CustomerSection";
import { ProductsSection } from "../components/ProductsSection";
import { ProductCard } from "../components/CardResult";
import { SalesSummary } from "../components/SalesSummary";
import { OrdersListTab } from "../components/OrdersListTab";
import { OrderCancelModal } from "../components/OrderCancelModal";
import { useSalesProductSearch } from "../hooks/useSalesProductSearch";
import { useOrderManagement } from "../hooks/useOrderManagement";


const SalesPage = () => {
  const [activeTab, setActiveTab] = useState("register");
  const [isEditing, setIsEditing] = useState(false);

  const [currentOrder, setCurrentOrder] = useState({
    customer: null,
    products: [],
    note: "",
    subtotal: 0,
    discount: 0,
    total: 0,
  });

  const {
    searchQuery: productQuery,
    setSearchQuery: setProductQuery,
    products,
    loading: loadingProducts,
  } = useSalesProductSearch();

  const {
    orders: ordersData,
    selectedOrder,
    showCancelModal,
    handleSaveEditedOrder,
    handleCancelOrder,
    handleConfirmCancel,
    handleCollectOrder,
    handleCloseCancelModal,
    handleCreateOrder,
  } = useOrderManagement();

  const handleClearSearch = () => {
    setProductQuery("");
  };

  const handleSelectProduct = (product) => {
    setCurrentOrder((prev) => {
      const existingProduct = prev.products.find((p) => p.id === product.id);

      if (existingProduct) {
        return {
          ...prev,
          products: prev.products.map((p) =>
            p.id === product.id ? { ...p, quantity: p.quantity + 1 } : p
          ),
        };
      } else {
        return {
          ...prev,
          products: [...prev.products, { ...product, quantity: 1 }],
        };
      }
    });
  };

  const handleEditOrder = (order) => {
    setCurrentOrder({
      ...order,
      products: [...order.products], 
    });
    setIsEditing(true);
    setActiveTab("register");
  };

  const handleFinishOrder = () => {
    if (isEditing) {
      handleSaveEditedOrder(currentOrder);
      
    } else {
      const newOrder = handleCreateOrder(currentOrder);
      alert(`Pedido #${newOrder.id} creado exitosamente`);
    }

    setCurrentOrder({
      customer: null,
      products: [],
      note: "",
      subtotal: 0,
      discount: 0,
      total: 0,
    });
    setIsEditing(false);
    setActiveTab("pending");
  };

  const handleAddNote = () => {
    const note = prompt("Ingrese una nota para el pedido:", currentOrder.note);
    if (note !== null) {
      setCurrentOrder({ ...currentOrder, note });
    }
  };

  const renderTabContent = () => {
    switch (activeTab) {
      case "register":
        return (
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
                        onAddToCart={() => handleSelectProduct(product)}
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
                cartItems={currentOrder.products}
                subtotal={currentOrder.subtotal}
                total={currentOrder.total}
                onAddItem={(product) => handleSelectProduct(product)}
                onDecrementItem={(productId) => {
                  setCurrentOrder(prev => ({
                    ...prev,
                    products: prev.products.map(p =>
                      p.id === productId ? { ...p, quantity: Math.max(1, p.quantity - 1) } : p
                    )
                  }));
                }}
                onRemoveItem={(productId) => {
                  setCurrentOrder(prev => ({
                    ...prev,
                    products: prev.products.filter(p => p.id !== productId)
                  }));
                }}
              />

              <div className="flex space-x-4">
                <Button
                  onClick={handleAddNote}
                  className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[131px] h-[40px]"
                >
                  <span>{isEditing ? "Editar nota" : "Agregar nota"}</span>
                </Button>
                <Button
                  onClick={handleFinishOrder}
                  className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[149px] h-[40px]"
                >
                  <span>{isEditing ? "Guardar cambios" : "Finalizar pedido"}</span>
                </Button>
              </div>
            </div>
          </div>
        );

      case "pending":
        return (
          <OrdersListTab
            orders={ordersData.pending}
            status="pending"
            onEdit={handleEditOrder}
            onCancel={handleCancelOrder}
            onCollect={handleCollectOrder}
          />
        );

      case "confirmed":
        return (
          <OrdersListTab orders={ordersData.confirmed} status="confirmed" />
        );

      case "cancelled":
        return (
          <OrdersListTab orders={ordersData.cancelled} status="cancelled" />
        );

      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen w-full flex flex-col">
      <OrderTabs activeTab={activeTab} onTabChange={setActiveTab} />

      <div className="bg-background rounded-2xl shadow-sm p-8 border border-border max-w-[1200px] mx-auto w-full">
        {activeTab === "register" && (
          <>
            <CustomerSection 
             preSelectedCustomer={isEditing ? currentOrder.customer : null}
             onCustomerChange={(customer) => setCurrentOrder({...currentOrder, customer})}
            />
            <ProductsSection
              productQuery={productQuery}
              setProductQuery={setProductQuery}
              handleClearSearch={handleClearSearch}
            />
          </>
        )}

        {renderTabContent()}
      </div>

      {showCancelModal && (
        <OrderCancelModal
          order={selectedOrder}
          onConfirm={handleConfirmCancel}
          onCancel={handleCloseCancelModal}
        />
      )}
    </div>
  );
};

export default SalesPage;