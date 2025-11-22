package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint GET /api/categories/{id} - Obtener por ID.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Obtener categoría por ID",
    description = "Obtiene los detalles de una categoría específica"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Categoría encontrada",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": true,
                  "message": "Categoría obtenida exitosamente",
                  "data": {
                    "id": 1,
                    "name": "Electrónica",
                    "description": "Dispositivos electrónicos y tecnología",
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
    )
})
public @interface GetCategoryByIdDoc {}

