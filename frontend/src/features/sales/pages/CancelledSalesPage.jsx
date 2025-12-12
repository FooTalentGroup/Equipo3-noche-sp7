import { OrdersListTab } from "../components/OrdersListTab";
import { useCancelledSales } from "../hooks/useCancelledSales";

export const CancelledSalesPage = () => {
  const { data: cancelledOrders, isLoading, error } = useCancelledSales();

  if (isLoading) {
    return <div className="p-8 text-center">Cargando ventas canceladas...</div>;
  }

  if (error) {
    return (
      <div className="p-8 text-center text-red-600">
        Error cargando ventas: {error.message || "Error desconocido"}
      </div>
    );
  }

  const orders = Array.isArray(cancelledOrders) ? cancelledOrders : (cancelledOrders?.content || []);

  return (
    <div className="h-full w-full">
      <OrdersListTab orders={orders} status="cancelled" />
    </div>
  );
};
