package com.stockia.stockia.exceptions.product;

public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String message) {
        super(message);
    }

    public DuplicateProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateProductException() {
        super("Producto duplicado. Ya existe un producto con el mismo nombre en esta categoría");
    }
}

