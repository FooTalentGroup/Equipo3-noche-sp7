package com.stockia.stockia.documentation.common;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotaciones reutilizables para respuestas de seguridad comunes en la documentación API.
 * Centraliza las respuestas 401 y 403 para evitar duplicación de código.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-23
 */
public class SecurityResponses {

    /**
     * Respuesta 401 - No autorizado (token ausente o inválido)
     */
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
        responseCode = "401",
        description = "No autorizado - Token ausente o inválido",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "Acceso no autorizado. Token inválido o ausente",
                  "data": null
                }
                """
            )
        )
    )
    public @interface Unauthorized {}

    /**
     * Respuesta 403 - Acceso denegado (rol ADMIN requerido)
     */
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
        responseCode = "403",
        description = "Acceso denegado - Se requiere rol ADMIN",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "Acceso denegado. No tienes permisos para realizar esta acción",
                  "data": null
                }
                """
            )
        )
    )
    public @interface ForbiddenAdmin {}

    /**
     * Respuesta 403 - Acceso denegado (rol ADMIN o MANAGER requerido)
     */
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
        responseCode = "403",
        description = "Acceso denegado - Se requiere rol ADMIN o MANAGER",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "Acceso denegado. No tienes permisos para realizar esta acción",
                  "data": null
                }
                """
            )
        )
    )
    public @interface ForbiddenAdminOrManager {}

    /**
     * Combinación de respuestas de seguridad para endpoints que requieren solo ADMIN.
     * Aplica automáticamente las respuestas 401 (No autorizado) y 403 (Acceso denegado - ADMIN).
     */
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Unauthorized
    @ForbiddenAdmin
    public @interface RequiresAdmin {}

    /**
     * Combinación de respuestas de seguridad para endpoints que requieren ADMIN o MANAGER.
     * Aplica automáticamente las respuestas 401 (No autorizado) y 403 (Acceso denegado - ADMIN o MANAGER).
     */
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Unauthorized
    @ForbiddenAdminOrManager
    public @interface RequiresAdminOrManager {}
}

