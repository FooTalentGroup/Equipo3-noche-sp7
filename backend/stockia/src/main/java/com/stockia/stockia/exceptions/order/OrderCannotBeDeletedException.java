package com.stockia.stockia.exceptions.order;

/**
 * Excepción lanzada cuando se intenta eliminar una orden confirmada.
 * De acuerdo a RN-02, las órdenes confirmadas no pueden eliminarse,
 * solo pueden anularse para mantener el registro histórico.
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
public class OrderCannotBeDeletedException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message Mensaje descriptivo del error
     */
    public OrderCannotBeDeletedException(String message) {
        super(message);
    }

    /**
     * Constructor por defecto con mensaje estándar.
     */
    public OrderCannotBeDeletedException() {
        super("No se puede eliminar una orden confirmada. Use la opción de cancelar para mantener el registro histórico.");
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause   Causa raíz de la excepción
     */
    public OrderCannotBeDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
