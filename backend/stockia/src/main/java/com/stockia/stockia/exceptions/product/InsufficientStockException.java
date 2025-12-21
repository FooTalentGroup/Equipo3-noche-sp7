package com.stockia.stockia.exceptions.product;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(Integer currentStock, Integer requiredQuantity) {
        super(String.format("Stock insuficiente. Stock actual: %d, cantidad requerida: %d",
                currentStock, requiredQuantity));
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
