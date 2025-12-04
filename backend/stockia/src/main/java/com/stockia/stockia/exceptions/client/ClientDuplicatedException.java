package com.stockia.stockia.exceptions.client;

/**
 * Excepción lanzada cuando se intenta registrar un cliente duplicado.
 * Se considera duplicado si ya existe un cliente con el mismo email o teléfono.
 *
 * @author StockIA Team
 */
public class ClientDuplicatedException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message mensaje de error descriptivo
     */
    public ClientDuplicatedException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message mensaje de error descriptivo
     * @param cause   causa original de la excepción
     */
    public ClientDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor por defecto con mensaje estándar.
     */
    public ClientDuplicatedException() {
        super("El cliente ya está registrado con ese correo o teléfono");
    }
}
