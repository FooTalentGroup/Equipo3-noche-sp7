package com.stockia.stockia.enums;

/**
 * Enum que representa los métodos de pago disponibles para las ventas.
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
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
