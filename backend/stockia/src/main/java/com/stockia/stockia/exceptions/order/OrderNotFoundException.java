package com.stockia.stockia.exceptions.order;

/**
 * Excepción lanzada cuando no se encuentra una orden por su ID o número de
 * orden.
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message Mensaje descriptivo del error
     */
    public OrderNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que genera mensaje automático para búsqueda por ID.
     * 
     * @param id ID de la orden no encontrada
     */
    public OrderNotFoundException(java.util.UUID id) {
        super(String.format("No se encontró la orden con ID: %s", id));
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause   Causa raíz de la excepción
     */
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
