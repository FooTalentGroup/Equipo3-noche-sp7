package com.stockia.stockia.enums;

/**
 * Enum que representa los métodos de pago disponibles para las ventas.
 */
public enum PaymentMethod {
    /**
     * Pago en efectivo.
     */
    CASH,

    /**
     * Pago con tarjeta (débito o crédito).
     */
    CARD,

    /**
     * Pago mediante transferencia bancaria.
     */
    TRANSFER
}
