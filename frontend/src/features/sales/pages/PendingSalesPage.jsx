import { useLocation, useNavigate } from "react-router";
import { Dialog, DialogContent } from "@/shared/components/ui/dialog";
import SalePayment from "../components/SalePayment";
import { OrdersListTab } from "../components/OrdersListTab";
import { OrderCancelModal } from "../components/OrderCancelModal";
import { usePendingSales } from "../hooks/usePendingSales";
import { useState } from "react";
import { useCancelSale } from "../hooks/useCancelSale";

export const PendingSalesPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const isPaymentModalOpen = location.pathname.startsWith('/sales/pending/payment/');
  const { mutateAsync: cancelSale } = useCancelSale();
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);

  const handleCancelOrder = (order) => {
    setSelectedOrder(order);
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    cancelSale(selectedOrder.id);
    setShowCancelModal(false);
    setSelectedOrder(null);
  };

  const handleCloseCancelModal = () => {
    setShowCancelModal(false);
    setSelectedOrder(null);
  };

  const { data: ordersData, isLoading, error } = usePendingSales();


  const handleEditOrder = (order) => {
    navigate(`/sales/edit/${order.id}`);
  };

  const handleCollectOrder = (order) => {
    navigate(`/sales/pending/payment/${order.id}`);
  };

  const handleModalChange = (open) => {
    if (!open) {
      navigate("/sales/pending", { replace: true });
    }
  };

  if (isLoading) {
    return <div className="p-8 text-center">Cargando ventas pendientes...</div>;
  }

  if (error) {
    return (
      <div className="p-8 text-center text-red-600">
        Error cargando ventas: {queries.pending.error?.message || "Error desconocido"}
      </div>
    );
  }

  return (
    <div className="h-full w-full">
      <OrdersListTab
        orders={ordersData.content}
        status="pending"
        onEdit={handleEditOrder}
        onCancel={handleCancelOrder}
        onCollect={handleCollectOrder}
        onConfirm={(orderId) => navigate(`/sales/pending/payment/${orderId}`)}
      />

      <Dialog open={isPaymentModalOpen} onOpenChange={handleModalChange}>
        <DialogContent className="max-w-2xl p-0! bg-stokia-neutral-50">
          <SalePayment onExternalClose={handleModalChange} />
        </DialogContent>
      </Dialog>

      {showCancelModal && (
        <OrderCancelModal
          onConfirm={handleConfirmCancel}
          onCancel={handleCloseCancelModal}
          order={selectedOrder}
        />
      )}
    </div>
  );
};