package com.stockia.stockia.exceptions.product;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(UUID id) {
        super("Producto no encontrado con ID: " + id);
    }

    public ProductNotFoundException() {
        super("Producto no encontrado");
    }
}
