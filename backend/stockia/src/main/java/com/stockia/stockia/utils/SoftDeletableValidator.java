package com.stockia.stockia.utils;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SoftDeletableValidator {

    public static void validateNotAlreadyDeleted(Boolean deleted, String entityType, Long entityId) {
        if (deleted != null && deleted) {
            String message = String.format("El/La %s ya está eliminado/a", entityType);
            log.warn("{} con ID {} ya está eliminado/a", capitalize(entityType), entityId);
            throw new IllegalStateException(message);
        }
    }

    public static void validateIsDeleted(Boolean deleted, String entityType, UUID entityId) {
        if (deleted == null || !deleted) {
            String message = String.format("El/La %s no está eliminado/a", entityType);
            log.warn("Intentando restaurar {} con ID {} que no está eliminado/a", entityType, entityId);
            throw new IllegalStateException(message);
        }
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
