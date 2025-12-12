import React, { useState, useEffect } from "react";
import { CancelledOrderCard } from "./CancelledOrderCard";
import { getCancelledOrders } from "../services/salesService";

export const CancelledOrdersTab = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchCancelledOrders = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await getCancelledOrders();
                let ordersList = [];

                if (Array.isArray(response)) {
                    ordersList = response;
                } else if (response?.data) {
                    if (Array.isArray(response.data)) {
                        ordersList = response.data;
                    } else if (response.data?.orders && Array.isArray(response.data.orders)) {
                        ordersList = response.data.orders;
                    } else if (response.data?.content && Array.isArray(response.data.content)) {
                        ordersList = response.data.content;
                    }
                } else if (response?.orders && Array.isArray(response.orders)) {
                    ordersList = response.orders;
                } else if (response?.content && Array.isArray(response.content)) {
                    ordersList = response.content;
                }

                setOrders(ordersList);
            } catch (err) {
                console.error("Error al cargar órdenes canceladas:", err);
                setError("Error al cargar las órdenes canceladas");
            } finally {
                setLoading(false);
            }
        };

        fetchCancelledOrders();
    }, []);

    if (loading) {
        return (
            <div className="col-span-full text-center p-8 text-muted-foreground">
                Cargando órdenes canceladas...
            </div>
        );
    }

    if (error) {
        return (
            <div className="col-span-full text-center p-8 text-red-600">
                {error}
            </div>
        );
    }

    if (orders.length === 0) {
        return (
            <div className="col-span-full text-center p-8 text-muted-foreground">
                No hay pedidos cancelados
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {orders.map((order) => (
                <CancelledOrderCard
                    key={order.id}
                    order={order}
                />
            ))}
        </div>
    );
};
