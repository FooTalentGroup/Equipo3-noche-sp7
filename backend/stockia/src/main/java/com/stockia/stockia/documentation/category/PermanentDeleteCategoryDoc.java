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
import com.stockia.stockia.documentation.common.SecurityResponses;

/**
 * Documentación del endpoint DELETE /api/categories/{id}/permanent - Eliminar permanentemente.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Eliminar categoría permanentemente",
    description = "Elimina físicamente la categoría de la base de datos. Esta acción es irreversible. " +
                  "<strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Categoría eliminada permanentemente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": true,
                  "message": "Categoría eliminada permanentemente",
                  "data": null
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
                  "message": "No se encontró la categoría con ID: 123e4567-e89b-12d3-a456-426614174001",
                  "data": null
                }
                """
            )
        )
    )
})
@SecurityResponses.RequiresAdmin
public @interface PermanentDeleteCategoryDoc {}
