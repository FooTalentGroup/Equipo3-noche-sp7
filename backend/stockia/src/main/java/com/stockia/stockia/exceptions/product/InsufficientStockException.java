package com.stockia.stockia.exceptions.product;

/**
 * Excepción lanzada cuando no hay stock suficiente para realizar un movimiento
 * de inventario.
 * 
 * @author StockIA Team
 */
public class InsufficientStockException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message Mensaje descriptivo del error
     */
    public InsufficientStockException(String message) {
        super(message);
    }

    /**
     * Constructor con stock actual y cantidad requerida.
     * 
     * @param currentStock     Stock actual disponible
     * @param requiredQuantity Cantidad requerida
     */
    public InsufficientStockException(Integer currentStock, Integer requiredQuantity) {
        super(String.format("Stock insuficiente. Stock actual: %d, cantidad requerida: %d",
                currentStock, requiredQuantity));
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause   Causa raíz de la excepción
     */
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
