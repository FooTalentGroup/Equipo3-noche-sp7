package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: GET /api/products/{id}
 * Obtiene un producto específico por su ID.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Obtener producto por ID",
    description = "Retorna la información completa de un producto específico"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto encontrado",
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto encontrado\",\"data\":{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"photoUrl\":null,\"currentStock\":10,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}}"
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
public @interface GetProductByIdDoc {
}

