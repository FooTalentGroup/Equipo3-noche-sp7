package com.stockia.stockia.enums;

/**
 * Enum que representa el estado del pago de una orden.
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
