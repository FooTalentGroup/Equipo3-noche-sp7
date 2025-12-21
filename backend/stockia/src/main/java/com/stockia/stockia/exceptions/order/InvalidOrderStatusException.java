package com.stockia.stockia.exceptions.order;

public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }

    public InvalidOrderStatusException(String currentStatus, String attemptedAction) {
        super(String.format(
                "No se puede %s una orden en estado %s",
                attemptedAction, currentStatus));
    }

    public InvalidOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
