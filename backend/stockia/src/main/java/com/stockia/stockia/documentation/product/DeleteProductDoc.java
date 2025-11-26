package com.stockia.stockia.documentation.product;

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
import com.stockia.stockia.documentation.common.SecurityResponses;

/**
 * Documentación del endpoint: DELETE /api/products/{id}
 * Marca un producto como eliminado (soft delete).
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Eliminar producto (soft delete)",
    description = "Marca el producto como eliminado sin borrarlo físicamente. Se guarda en el historial. " +
                  "<strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto eliminado exitosamente",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto eliminado exitosamente\",\"data\":null}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"No se encontró el producto con ID: 123e4567-e89b-12d3-a456-426614174000\",\"data\":null}"
            )
        )
    )
})
@SecurityResponses.RequiresAdmin
public @interface DeleteProductDoc {
}
