package com.stockia.stockia.exceptions.client;

public class ClientDuplicatedException extends RuntimeException {

    public ClientDuplicatedException(String message) {
        super(message);
    }

    public ClientDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientDuplicatedException() {
        super("El cliente ya está registrado con ese correo o teléfono");
    }
}
