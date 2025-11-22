package com.stockia.stockia.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase de utilidad para validaciones comunes de entidades con soft delete.
 * Reduce código duplicado en servicios que manejan soft delete.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-21
 */
@Slf4j
public class SoftDeletableValidator {

    /**
     * Valida que una entidad no esté ya eliminada antes de intentar eliminarla.
     *
     * @param deleted estado de eliminación de la entidad
     * @param entityType tipo de entidad (ej: "producto", "categoría")
     * @param entityId ID de la entidad
     * @throws IllegalStateException si la entidad ya está eliminada
     */
    public static void validateNotAlreadyDeleted(Boolean deleted, String entityType, Long entityId) {
        if (deleted != null && deleted) {
            String message = String.format("El/La %s ya está eliminado/a", entityType);
            log.warn("{} con ID {} ya está eliminado/a", capitalize(entityType), entityId);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Valida que una entidad esté eliminada antes de intentar restaurarla.
     *
     * @param deleted estado de eliminación de la entidad
     * @param entityType tipo de entidad (ej: "producto", "categoría")
     * @param entityId ID de la entidad
     * @throws IllegalStateException si la entidad no está eliminada
     */
    public static void validateIsDeleted(Boolean deleted, String entityType, Long entityId) {
        if (deleted == null || !deleted) {
            String message = String.format("El/La %s no está eliminado/a", entityType);
            log.warn("Intentando restaurar {} con ID {} que no está eliminado/a", entityType, entityId);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Capitaliza la primera letra de un string.
     *
     * @param text texto a capitalizar
     * @return texto con la primera letra en mayúscula
     */
    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

