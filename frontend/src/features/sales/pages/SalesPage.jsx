import React, { useState } from "react";
import { AlertCircle } from "lucide-react";
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
import { OrderNoteModal } from "../components/OrderNoteModal";
import { OrderSuccessModal } from "../components/OrderSuccessModal";
import { useCart } from "../hooks/useCart";


const SalesPage = () => {
  const [activeTab, setActiveTab] = useState("register");
  const [isEditing, setIsEditing] = useState(false);
  const [editingOrderId, setEditingOrderId] = useState(null);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [orderNote, setOrderNote] = useState("");
  const [showNoteModal, setShowNoteModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  

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

  
  const {
    items: cartItems,
    discount,
    addItem,
    removeItem,
    applyDiscount,
    clearCart,
    loadFromOrder,
    subtotal,
    discountAmount,
    total,
  } = useCart();

  const handleClearSearch = () => {
    setProductQuery("");
  };

 const handleSelectProduct = (product) => {
    addItem(product);
  };
  const isCheckoutEnabled = selectedCustomer !== null && cartItems.length > 0;

  const handleEditOrder = (order) => {
   
    loadFromOrder(order);
    setSelectedCustomer(order.customer);
    setOrderNote(order.note || "");
    setEditingOrderId(order.id);
    setIsEditing(true);
    setActiveTab("register");
  };

  const handleFinishOrder = () => {
    if (!selectedCustomer) {
      alert("Por favor selecciona un cliente");
      return;
    }

    if (cartItems.length === 0) {
      alert("Por favor agrega al menos un producto");
      return;
    }

    const orderData = {
      customer: selectedCustomer,
      products: cartItems,
      note: orderNote,
      subtotal: subtotal,
      discount: discountAmount,
      discountType: discount?.type || null,
      discountValue: discount?.value || null,
      total: total,
    };

    if (isEditing) {
      handleSaveEditedOrder({
        ...orderData,
        id: editingOrderId,
      });

      clearCart();
      setSelectedCustomer(null);
      setOrderNote("");
      setIsEditing(false);
      setEditingOrderId(null);
      setActiveTab("pending");
      return;
    
    } else {
      handleCreateOrder(orderData);
    }

    clearCart();
    setSelectedCustomer(null);
    setOrderNote("");
    setIsEditing(false);
    setEditingOrderId(null);
    setActiveTab("pending");
    setTimeout(() => {
      setShowSuccessModal(true);
    }, 200);
  };

  const handleCloseSuccessModal = () => {
  setShowSuccessModal(false);
}; 

  const handleAddNote = () => {
  setShowNoteModal(true);
};

const handleSaveNote = (note) => {
  setOrderNote(note);
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
                cartItems={cartItems}
                subtotal={subtotal}
                total={total}
                discount={discount}
                onRemoveItem={removeItem}
                onApplyDiscount={applyDiscount}
                onRemoveDiscount={() => applyDiscount(null)}
              />

              <div className="flex space-x-4">
                <Button
                  onClick={handleAddNote}
                  className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[131px] h-[40px] hover:bg-stokia-neutral-50"
                >
                  <span>{isEditing || orderNote ? "Editar nota" : "Agregar nota"}</span>
                </Button>
                <Button
                  onClick={handleFinishOrder}
                  className={`
                      py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm 
                      min-w-[149px] h-[40px]
                      ${isCheckoutEnabled
                        ? "bg-btn-primary text-white hover:bg-btn-primary/80"
                        : "bg-stokia-neutral-50 text-foreground cursor-not-allowed"
                      }
                    `}
                  disabled={!selectedCustomer || cartItems.length === 0}
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
             preSelectedCustomer={isEditing ? selectedCustomer : null}
             onCustomerChange={setSelectedCustomer}
             disableRemove={isEditing}
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
      {showNoteModal && (
        <OrderNoteModal
          isOpen={showNoteModal}
          onClose={() => setShowNoteModal(false)}
          onSave={handleSaveNote}
          initialNote={orderNote}
        />
      )}
    {showSuccessModal && (
      <OrderSuccessModal
        isOpen={showSuccessModal}
        onClose={handleCloseSuccessModal}
        orderNote={orderNote}
      />
    )}

    </div>
  );
};

export default SalesPage;
