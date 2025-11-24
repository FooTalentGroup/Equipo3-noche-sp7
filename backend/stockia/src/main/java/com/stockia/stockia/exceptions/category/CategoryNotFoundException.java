package com.stockia.stockia.exceptions.category;

import java.util.UUID;

/**
 * Excepción lanzada cuando no se encuentra una categoría solicitada.
 * Se utiliza principalmente al validar la existencia de una categoría
 * antes de asignarla a un producto.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message mensaje de error descriptivo
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message mensaje de error descriptivo
     * @param cause   causa original de la excepción
     */
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor conveniente con ID de la categoría.
     * Genera automáticamente un mensaje descriptivo con el ID buscado.
     *
     * @param id ID de la categoría no encontrada
     */
    public CategoryNotFoundException(UUID id) {
        super("Categoría no encontrada con ID: " + id);
    }

    /**
     * Constructor por defecto con mensaje estándar.
     */
    public CategoryNotFoundException() {
        super("Categoría no encontrada");
    }
}
