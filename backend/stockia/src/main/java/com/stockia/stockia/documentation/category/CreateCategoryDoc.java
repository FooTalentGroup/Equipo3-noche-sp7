package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint POST /api/categories - Crear Categoría.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Crear nueva categoría",
    description = "Crea una nueva categoría de productos. El nombre debe ser único. " +
                  "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "201",
        description = "Categoría creada exitosamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": true,
                  "message": "Categoría creada exitosamente",
                  "data": {
                    "id": 8,
                    "name": "Deportes",
                    "description": "Artículos deportivos y equipamiento",
                    "isActive": true,
                    "productCount": 0
                  }
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "Errores de validación",
                  "data": {
                    "fields": {
                      "name": "El nombre de la categoría es obligatorio"
                    },
                    "errors": [
                      "El nombre de la categoría es obligatorio"
                    ]
                  }
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "Ya existe una categoría con ese nombre",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "Ya existe una categoría con el nombre: Deportes",
                  "data": null
                }
                """
            )
        )
    ),
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
    ),
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
})
public @interface CreateCategoryDoc {}

