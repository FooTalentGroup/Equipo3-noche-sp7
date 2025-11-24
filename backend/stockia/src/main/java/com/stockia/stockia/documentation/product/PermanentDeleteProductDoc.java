package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: DELETE /api/products/{id}/permanent
 * Elimina permanentemente un producto de la base de datos.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Eliminar producto permanentemente",
    description = "Elimina físicamente el producto de la base de datos. Esta acción es irreversible"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto eliminado permanentemente",
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto eliminado permanentemente\",\"data\":null}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado",
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\"success\":false,\"message\":\"No se encontró el producto con ID: 999\",\"data\":null}"
            )
        )
    )
})
public @interface PermanentDeleteProductDoc {
}

