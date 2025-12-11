import { useState } from "react";
import { MOCK_ORDERS } from "../utils/mockOrders";

export const useOrderManagement = () => {
    
  const [orders, setOrders] = useState(MOCK_ORDERS);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);

  const handleSaveEditedOrder = (editedOrder) => {
    setOrders((prev) => ({
      ...prev,
      pending: prev.pending.map((o) =>
        o.id === editedOrder.id ? editedOrder : o
      ),
    }));
    setShowEditModal(false);
    setSelectedOrder(null);
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

  const handleCloseEditModal = () => {
    setShowEditModal(false);
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
    showEditModal,
    handleSaveEditedOrder,
    handleCancelOrder,
    handleConfirmCancel,
    handleCloseEditModal,
    handleCloseCancelModal,
  };
};