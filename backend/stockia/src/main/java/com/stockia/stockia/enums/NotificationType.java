package com.stockia.stockia.enums;

/**
 * Enum que define los tipos de notificaciones del sistema.
 */
public enum NotificationType {
    /**
     * Alerta de stock bajo - cuando el stock actual es <= al stock mínimo
     */
    LOW_STOCK,

    /**
     * Alerta de stock agotado - cuando el stock llega a 0
     */
    OUT_OF_STOCK,

    /**
     * Notificación de venta exitosa (podría utilizarse después)
     */
    SALE_SUCCESS,

    /**
     * Notificación de error del sistema (para manejar errores al notificar -> a
     * futuro)
     */
    ERROR
}
