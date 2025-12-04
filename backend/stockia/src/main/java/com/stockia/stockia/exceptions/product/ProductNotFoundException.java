package com.stockia.stockia.exceptions.product;

import java.util.UUID;

/**
 * Excepción lanzada cuando no se encuentra un producto solicitado.
 * Típicamente se lanza al buscar un producto por ID que no existe
 * o que ha sido eliminado (soft delete).
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message mensaje de error descriptivo
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message mensaje de error descriptivo
     * @param cause   causa original de la excepción
     */
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor conveniente con ID del producto.
     * Genera automáticamente un mensaje descriptivo con el ID buscado.
     *
     * @param id ID del producto no encontrado
     */
    public ProductNotFoundException(UUID id) {
        super("Producto no encontrado con ID: " + id);
    }

    /**
     * Constructor por defecto con mensaje estándar.
     */
    public ProductNotFoundException() {
        super("Producto no encontrado");
    }
}
