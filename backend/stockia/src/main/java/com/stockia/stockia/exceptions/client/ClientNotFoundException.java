package com.stockia.stockia.exceptions.client;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNotFoundException(UUID id) {
        super("Cliente no encontrado con ID: " + id);
    }

    public ClientNotFoundException() {
        super("Cliente no encontrado");
    }
}
