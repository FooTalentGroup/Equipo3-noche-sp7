package com.stockia.stockia.enums;

/**
 * Enum que representa el estado del pago de una orden.
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
public enum PaymentStatus {
    /**
     * Pago pendiente.
     * El pago aún no ha sido recibido o confirmado.
     */
    PENDING,

    /**
     * Pago completado.
     * El pago fue recibido y confirmado.
     */
    PAID,

    /**
     * Pago reembolsado.
     * El pago fue devuelto al cliente (típicamente por cancelación).
     */
    REFUNDED
}
