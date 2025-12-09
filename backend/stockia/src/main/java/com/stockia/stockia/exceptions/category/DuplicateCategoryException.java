package com.stockia.stockia.exceptions.category;

/**
 * Excepción lanzada cuando se intenta crear una categoría con un nombre
 * duplicado.
 */
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String message) {
        super(message);
    }

    public DuplicateCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
