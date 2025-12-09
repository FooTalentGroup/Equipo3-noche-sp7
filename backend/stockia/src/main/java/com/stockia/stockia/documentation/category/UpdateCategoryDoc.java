package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.stockia.stockia.documentation.common.SecurityResponses;

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
    summary = "Actualizar categoría (actualización parcial)",
    description = "Actualiza los datos de una categoría existente. " +
                  "<br/><strong>Todos los campos son opcionales</strong> - solo envía los campos que deseas actualizar. " +
                  "<br/>Los campos no enviados mantendrán su valor actual. " +
                  "<br/><br/>Ejemplo de actualización solo del nombre: <code>{\"name\": \"nuevo nombre\"}</code>" +
                  "<br/>Ejemplo de actualización de descripción: <code>{\"description\": \"nueva descripción\"}</code>" +
                  "<br/><br/><strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key"),
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos de la categoría a actualizar (todos los campos son opcionales)",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Actualizar solo nombre",
                    summary = "Actualización parcial - solo nombre",
                    value = "{\"name\": \"Electrónica y Tecnología\"}"
                ),
                @ExampleObject(
                    name = "Actualizar nombre y descripción",
                    summary = "Actualización parcial - nombre y descripción",
                    value = "{\"name\": \"Electrónica\", \"description\": \"Dispositivos electrónicos y computadoras\"}"
                ),
                @ExampleObject(
                    name = "Actualizar todos los campos",
                    summary = "Actualización completa",
                    value = "{\"name\": \"Electrónica y Tecnología\", \"description\": \"Dispositivos electrónicos, computadoras y accesorios\", \"isActive\": true}"
                )
            }
        )
    )
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
                    "id": "123e4567-e89b-12d3-a456-426614174001",
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
        responseCode = "400",
        description = "Datos inválidos o formato incorrecto",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "statusCode": 400,
                  "errorCode": "VALIDATION_ERROR",
                  "message": "Falló la validación de los campos",
                  "details": ["name: El nombre no puede exceder 50 caracteres"],
                  "timestamp": "2025-11-26T15:10:38.908929300Z",
                  "path": "/api/categories/123e4567-e89b-12d3-a456-426614174001"
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
                  "statusCode": 404,
                  "errorCode": "NOT_FOUND",
                  "message": "No se encontró la categoría con ID: 123e4567-e89b-12d3-a456-426614174001",
                  "details": ["La categoría especificada no existe en el sistema"],
                  "timestamp": "2025-11-26T15:10:38.908929300Z",
                  "path": "/api/categories/123e4567-e89b-12d3-a456-426614174001"
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
                  "statusCode": 409,
                  "errorCode": "CONFLICT",
                  "message": "Ya existe una categoría con el nombre: Oficina",
                  "details": ["El nombre de la categoría debe ser único"],
                  "timestamp": "2025-11-26T15:10:38.908929300Z",
                  "path": "/api/categories/123e4567-e89b-12d3-a456-426614174001"
                }
                """
            )
        )
    )
})
@SecurityResponses.RequiresAdmin
public @interface UpdateCategoryDoc {}
