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
 * Documentación del endpoint PUT /api/categories/{id} - Actualizar.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Actualizar categoría",
    description = "Actualiza los datos de una categoría existente. " +
                  "<strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Categoría actualizada exitosamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": true,
                  "message": "Categoría actualizada exitosamente",
                  "data": {
                    "id": 1,
                    "name": "Electrónica y Tecnología",
                    "description": "Dispositivos electrónicos, computadoras y accesorios",
                    "isActive": true,
                    "productCount": 5
                  }
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Categoría no encontrada",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "No se encontró la categoría con ID: 999",
                  "data": null
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "El nuevo nombre ya existe",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": false,
                  "message": "Ya existe una categoría con el nombre: Oficina",
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
})
public @interface UpdateCategoryDoc {}

