package com.stockia.stockia.enums;

/**
 * Enum que representa los estados posibles de una orden de venta.
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
public enum OrderStatus {
    /**
     * Orden creada pero no confirmada.
     * En este estado el stock ya está reservado pero la venta no es definitiva.
     */
    PENDING,

    /**
     * Orden confirmada.
     * El stock ha sido decrementado y la venta es definitiva.
     */
    CONFIRMED,

    /**
     * Orden entregada al cliente.
     * Estado final positivo del ciclo de vida de la orden.
     */
    DELIVERED,

    /**
     * Orden anulada.
     * El stock fue restaurado y se registró el motivo de cancelación.
     */
    CANCELLED
}
