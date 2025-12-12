import React from "react";
import { OrderCard } from "./OrderCard";

export const OrdersListTab = ({ orders, status, onEdit, onCancel }) => {
  const getEmptyMessage = () => {
    switch (status) {
      case "pending":
        return "No hay pedidos pendientes de cobro";
      case "confirmed":
        return "No hay pedidos confirmados";
      case "cancelled":
        return "No hay pedidos cancelados";
      default:
        return "No hay pedidos";
    }
  };

  if (orders.length === 0) {
    return (
      <div className="col-span-full text-center p-8 text-muted-foreground">
        {getEmptyMessage()}
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      {orders.map((order) => (
        <OrderCard
          key={order.id}
          order={order}
          status={status}
          onEdit={onEdit}
          onCancel={onCancel}
        />
      ))}
    </div>
  );
};