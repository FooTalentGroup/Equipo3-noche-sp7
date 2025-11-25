package com.stockia.stockia.exceptions.order;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida
 * debido al estado actual de la orden.
 * Por ejemplo: confirmar una orden ya confirmada, cancelar una orden entregada,
 * etc.
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
public class InvalidOrderStatusException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message Mensaje descriptivo del error
     */
    public InvalidOrderStatusException(String message) {
        super(message);
    }

    /**
     * Constructor que genera mensaje para transición de estado inválida.
     * 
     * @param currentStatus   Estado actual de la orden
     * @param attemptedAction Acción que se intentó realizar
     */
    public InvalidOrderStatusException(String currentStatus, String attemptedAction) {
        super(String.format(
                "No se puede %s una orden en estado %s",
                attemptedAction, currentStatus));
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause   Causa raíz de la excepción
     */
    public InvalidOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
