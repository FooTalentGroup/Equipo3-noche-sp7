package com.stockia.stockia.exceptions.category;

/**
 * Excepción lanzada cuando se intenta crear una categoría con un nombre duplicado.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String message) {
        super(message);
    }

    public DuplicateCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

