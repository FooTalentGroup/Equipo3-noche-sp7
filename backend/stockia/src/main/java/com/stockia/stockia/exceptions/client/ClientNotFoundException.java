package com.stockia.stockia.exceptions.client;

import java.util.UUID;

/**
 * Excepción lanzada cuando no se encuentra un cliente solicitado.
 * Típicamente se lanza al buscar un cliente por ID, email o teléfono
 * que no existe en el sistema.
 *
 * @author StockIA Team
 */
public class ClientNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message mensaje de error descriptivo
     */
    public ClientNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message mensaje de error descriptivo
     * @param cause   causa original de la excepción
     */
    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor conveniente con ID del cliente.
     * Genera automáticamente un mensaje descriptivo con el ID buscado.
     *
     * @param id ID del cliente no encontrado
     */
    public ClientNotFoundException(UUID id) {
        super("Cliente no encontrado con ID: " + id);
    }

    /**
     * Constructor por defecto con mensaje estándar.
     */
    public ClientNotFoundException() {
        super("Cliente no encontrado");
    }
}
