import { useState } from "react";


export const useOrderManagement = () => {

  const [orders, setOrders] = useState({
    pending: [],
    confirmed: [],
    cancelled: []
  });

  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [nextOrderId, setNextOrderId] = useState(1);

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

  const handleCollectOrder = (order) => {
    setSelectedOrder(order);
    setShowPaymentModal(true);
  };

  const handleConfirmPayment = async (order, receivedAmount, change) => {
    if (!order) return;

    try {
      const orderPayload = {
        customerId: order.customer.id,
        items: order.products.map(product => ({
          productId: product.id,
          quantity: product.quantity,
          unitPrice: product.price
        })),
        paymentMethod: "CASH",
        paymentNote: order.note || `Pago en efectivo. Recibido: $${receivedAmount.toLocaleString("es-CL")}, Cambio: $${change.toLocaleString("es-CL")}`,
        discountAmount: order.discount || 0
      };

      const { createSale } = await import('../services/salesService');
      const createdOrder = await createSale(orderPayload);

      setOrders((prev) => ({
        ...prev,
        pending: prev.pending.filter((o) => o.id !== order.id),
        confirmed: [...prev.confirmed, {
          ...order,
          status: "confirmed",
          confirmedAt: new Date().toISOString(),
          receivedAmount,
          change,
          backendId: createdOrder.id || createdOrder.data?.id,
          orderNumber: createdOrder.orderNumber || createdOrder.data?.orderNumber
        }],
      }));

      return createdOrder;
    } catch (error) {
      throw new Error(error.response?.data?.message || error.message || "Error al procesar el pago");
    }
  };

  const handleClosePaymentModal = () => {
    setShowPaymentModal(false);
    setSelectedOrder(null);
  };

  return {
    orders,
    selectedOrder,
    showCancelModal,
    showPaymentModal,
    handleCreateOrder,
    handleSaveEditedOrder,
    handleCancelOrder,
    handleConfirmCancel,
    handleCloseCancelModal,
    handleCollectOrder,
    handleConfirmPayment,
    handleClosePaymentModal,
  };
};