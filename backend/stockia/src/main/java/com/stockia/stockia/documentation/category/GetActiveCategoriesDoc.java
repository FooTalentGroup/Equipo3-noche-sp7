package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint GET /api/categories/active - Solo Activas.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Listar categorías activas",
    description = "Obtiene solo las categorías que están activas (isActive = true). " +
                  "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Lista de categorías activas obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": true,
                  "message": "Categorías activas obtenidas exitosamente",
                  "data": [
                    {
                      "id": 1,
                      "name": "Electrónica",
                      "description": "Dispositivos electrónicos y tecnología",
                      "isActive": true,
                      "productCount": 5
                    }
                  ]
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
public @interface GetActiveCategoriesDoc {}

