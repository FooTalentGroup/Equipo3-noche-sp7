package com.stockia.stockia.exceptions.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(java.util.UUID id) {
        super(String.format("No se encontró la orden con ID: %s", id));
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
