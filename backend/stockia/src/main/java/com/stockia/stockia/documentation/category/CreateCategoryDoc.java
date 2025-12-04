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
                  "statusCode": 400,
                  "errorCode": "VALIDATION_ERROR",
                  "message": "Falló la validación de los campos",
                  "details": ["name: El nombre de la categoría es obligatorio"],
                  "timestamp": "2025-11-26T15:10:38.908929300Z",
                  "path": "/api/categories"
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
                  "statusCode": 409,
                  "errorCode": "CONFLICT",
                  "message": "Ya existe una categoría con el nombre: Deportes",
                  "details": ["El nombre de la categoría debe ser único"],
                  "timestamp": "2025-11-26T15:10:38.908929300Z",
                  "path": "/api/categories"
                }
                """
            )
        )
    )
})
@SecurityResponses.RequiresAdminOrManager
public @interface CreateCategoryDoc {}

