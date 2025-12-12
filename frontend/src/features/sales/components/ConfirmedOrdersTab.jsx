import React, { useState, useEffect, useMemo } from "react";
import { ConfirmedOrderCard } from "./ConfirmedOrderCard";
import { getConfirmedOrdersToday } from "../services/salesService";

export const ConfirmedOrdersTab = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchConfirmedOrders = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await getConfirmedOrdersToday();
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
                console.error("Error al cargar órdenes confirmadas:", err);
                setError("Error al cargar las órdenes confirmadas");
            } finally {
                setLoading(false);
            }
        };

        fetchConfirmedOrders();
    }, []);

    const todayOrders = useMemo(() => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);

        return orders.filter(order => {
            const orderDate = order.confirmedAt || order.orderDate || order.createdAt;
            if (!orderDate) return false;

            const date = new Date(orderDate);
            return date >= today && date < tomorrow;
        });
    }, [orders]);

    if (loading) {
        return (
            <div className="col-span-full text-center p-8 text-muted-foreground">
                Cargando órdenes confirmadas...
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

    if (todayOrders.length === 0) {
        return (
            <div className="col-span-full text-center p-8 text-muted-foreground">
                No hay pedidos confirmados hoy
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {todayOrders.map((order) => (
                <ConfirmedOrderCard
                    key={order.id}
                    order={order}
                />
            ))}
        </div>
    );
};
