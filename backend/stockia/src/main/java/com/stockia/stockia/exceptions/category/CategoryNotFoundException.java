package com.stockia.stockia.exceptions.category;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryNotFoundException(UUID id) {
        super("Categoría no encontrada con ID: " + id);
    }

    public CategoryNotFoundException() {
        super("Categoría no encontrada");
    }
}
