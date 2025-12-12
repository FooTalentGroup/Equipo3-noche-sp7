import { useState } from "react";


export const useOrderManagement = () => {
    
 const [orders, setOrders] = useState({
    pending: [],
    confirmed: [],
    cancelled: []
  });
  
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [nextOrderId, setNextOrderId] = useState(1);
  // const [showEditModal, setShowEditModal] = useState(false);

  const handleCreateOrder = (orderData) => {
    const newOrder = {
      id: nextOrderId,
      customer: orderData.customer,
      products: orderData.products,
      note: orderData.note || "",
      subtotal: orderData.subtotal,
      discount: orderData.discount,
      discountType: orderData.discountType,
      discountValue: orderData.discountValue,
      total: orderData.total,
      status: "pending",
      createdAt: new Date().toISOString()
    };

    setOrders((prev) => ({
      ...prev,
      pending: [...prev.pending, newOrder]
    }));

    setNextOrderId(prev => prev + 1);
    
    return newOrder;
  };

  const handleSaveEditedOrder = (editedOrder) => {
    setOrders((prev) => ({
      ...prev,
      pending: prev.pending.map((o) =>
        o.id === editedOrder.id ? {
          ...editedOrder,
          updatedAt: new Date().toISOString()
        } : o
      ),
    }));
  };

  const handleCancelOrder = (orderId) => {
    const order = orders.pending.find((o) => o.id === orderId);
    if (!order) return;

    setSelectedOrder(order);
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    if (!selectedOrder) return;

    setOrders((prev) => ({
      ...prev,
      pending: prev.pending.filter((o) => o.id !== selectedOrder.id),
      cancelled: [...prev.cancelled, { ...selectedOrder, status: "cancelled" }],
    }));

    setShowCancelModal(false);
    setSelectedOrder(null);
  };


  const handleCloseCancelModal = () => {
    setShowCancelModal(false);
    setSelectedOrder(null);
  };

  return {
    orders,
    selectedOrder,
    showCancelModal,
    handleCreateOrder,
    handleSaveEditedOrder,
    handleCancelOrder,
    handleConfirmCancel,
    handleCloseCancelModal,
  };
};