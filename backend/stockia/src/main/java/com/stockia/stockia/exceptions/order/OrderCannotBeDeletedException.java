package com.stockia.stockia.exceptions.order;

public class OrderCannotBeDeletedException extends RuntimeException {

    public OrderCannotBeDeletedException(String message) {
        super(message);
    }

    public OrderCannotBeDeletedException() {
        super("No se puede eliminar una orden confirmada. Use la opción de cancelar para mantener el registro histórico.");
    }

    public OrderCannotBeDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
