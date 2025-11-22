package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
    description = "Marca el producto como eliminado sin borrarlo físicamente. Se guarda en el historial"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto eliminado exitosamente",
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto eliminado exitosamente\",\"data\":null}"
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
public @interface DeleteProductDoc {
}

