import React, { useState, useEffect } from "react";
import { Button } from "@/shared/components/ui/button";
import { CustomerSection } from "../components/CustomerSection";
import { ProductsSection } from "../components/ProductsSection";
import { SalesSummary } from "../components/SalesSummary";
import { useSalesProductSearch } from "../hooks/useSalesProductSearch";
import { useOrderManagement } from "../hooks/useOrderManagement";
import { OrderNoteModal } from "../components/OrderNoteModal";
import { OrderSuccessModal } from "../components/OrderSuccessModal";
import { useCart } from "../hooks/useCart";
import { useParams } from "react-router";
import { useGetOrder } from "../hooks/useGetOrder";
import { ProductCard } from "../components/CardResult";
import { useUpdateOrder } from "../hooks/useUpdateOrder";

const NewSalePage = () => {
  const { orderId } = useParams();

  const [isEditing, setIsEditing] = useState(false);
  const [editingOrderId, setEditingOrderId] = useState(null);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [orderNote, setOrderNote] = useState("");
  const [showNoteModal, setShowNoteModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [customerSectionKey, setCustomerSectionKey] = useState(0);
 

  const {
    data: orderResponse,
    isLoading: loadingOrder,
    refetch: fetchOrder
  } = useGetOrder(orderId);

  const {
    searchQuery: productQuery,
    setSearchQuery: setProductQuery,
    products,
    loading: loadingProducts,
  } = useSalesProductSearch();

  const { handleCreateOrder, isPending: isCreating } = useOrderManagement();
  const { handleUpdateOrder, isPending: isUpdating } = useUpdateOrder();

  const isPending = isCreating || isUpdating;

  const {
    items: cartItems,
    discount,
    addItem,
    removeItem,
    updateItemQuantity,
    applyDiscount,
    clearCart,
    loadFromOrder,
    subtotal,
    discountAmount,
    total,
  } = useCart();

  useEffect(() => {
    if (!orderId) return;
    fetchOrder();
  }, [orderId]);

  useEffect(() => {
    if (!orderResponse) return;

    const order = orderResponse;

    setSelectedCustomer({
      id: order.customerId,
      name: order.customerName,
    });

    setOrderNote(order.paymentNote || "");

    setEditingOrderId(order.id);
    setIsEditing(true);

    loadFromOrder({
      items: order.items.map((item) => ({
        id: item.product.id,
        name: item.product.name,
        quantity: item.quantity,
        price: item.unitPrice,
        stock: item.product.currentStock,
        photoUrl: item.product.photoUrl,
      })),
      discountAmount: order.discountAmount,
    });

  }, [orderResponse]);


  const handleClearSearch = () => {
    setProductQuery("");
  };

  const handleSelectProduct = (product) => {
    addItem(product);
  };

  const isCheckoutEnabled = selectedCustomer !== null && cartItems.length > 0;

  const handleFinishOrder = async () => {
    if (!selectedCustomer) {
      alert("Por favor selecciona un cliente");
      return;
    }

    if (cartItems.length === 0) {
      alert("Por favor agrega al menos un producto");
      return;
    }

    const orderData = {
      customerId: selectedCustomer.id,
      items: cartItems.map((item) => ({
        productId: item.id,
        quantity: item.quantity,
        unitPrice: item.price,
      })),
      discountAmount: discountAmount || 0,
      paymentNote: orderNote,
      paymentMethod: "CASH",
    };

    try {
      if (isEditing) {
        await handleUpdateOrder({ 
          orderId: editingOrderId, 
          orderData 
        });
      } else {
        await handleCreateOrder(orderData);
      }

      clearCart();
      setSelectedCustomer(null);
      setOrderNote("");
      setProductQuery("");
      setIsEditing(false);
      setEditingOrderId(null);
      setCustomerSectionKey(prev => prev + 1);
      setTimeout(() => setShowSuccessModal(true), 200);
    } catch (error) {
      alert("Error al procesar el pedido. Por favor intente nuevamente.");
    }
  };

  if (loadingOrder) {
    return (
      <div className="min-h-screen flex items-center justify-center text-muted-foreground">
        Cargando orden...
      </div>
    );
  }

  return (
    <div className="min-h-screen w-full flex flex-col">
      <div className="bg-background rounded-2xl shadow-sm p-8 border border-border max-w-[1200px] mx-auto w-full">
        <CustomerSection
          key={customerSectionKey}
          preSelectedCustomer={isEditing ? selectedCustomer : null}
          onCustomerChange={setSelectedCustomer}
          disableRemove={isEditing}
        />

        <ProductsSection
          productQuery={productQuery}
          setProductQuery={setProductQuery}
          handleClearSearch={handleClearSearch}
        />

        <div className="flex-grow grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-8 items-start mt-8">
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
                  {products.map((product) => {
                    const cartItem = cartItems.find(item => item.id === product.id);
                    return (
                      <ProductCard
                        key={product.id || product.name}
                        id={product.id}
                        name={product.name}
                        stock={product.currentStock}
                        price={product.price}
                        imageUrl={product.photoUrl}
                        onAddToCart={handleSelectProduct}
                        cartItem={cartItem}
                      />
                    );
                  })}
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
              onUpdateQuantity={updateItemQuantity}
              onApplyDiscount={applyDiscount}
              onRemoveDiscount={() => applyDiscount(null)}
            />

            <div className="flex space-x-4">
              <Button
                onClick={() => setShowNoteModal(true)}
                className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[131px] h-10 hover:bg-stokia-neutral-50"
              >
                <span>
                  {isEditing || orderNote ? "Editar nota" : "Agregar nota"}
                </span>
              </Button>

              <Button
                onClick={handleFinishOrder}
                disabled={!isCheckoutEnabled || isPending}
                className={`py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[149px] h-10
                  ${isCheckoutEnabled
                    ? "bg-btn-primary text-white hover:bg-btn-primary/80"
                    : "bg-stokia-neutral-50 text-foreground cursor-not-allowed"
                  }`}
              >
                <span>{isEditing ? "Guardar cambios" : "Finalizar pedido"}</span>
              </Button>
            </div>
          </div>
        </div>
      </div>

      {showNoteModal && (
        <OrderNoteModal
          isOpen={showNoteModal}
          onClose={() => setShowNoteModal(false)}
          onSave={setOrderNote}
          initialNote={orderNote}
        />
      )}

      {showSuccessModal && (
        <OrderSuccessModal
          isOpen={showSuccessModal}
          onClose={() => setShowSuccessModal(false)}
          orderNote={orderNote}
        />
      )}
    </div>
  );
};

export default NewSalePage;
