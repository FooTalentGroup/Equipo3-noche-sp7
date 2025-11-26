package com.stockia.stockia.documentation.product;

import com.stockia.stockia.documentation.common.SecurityResponses;
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
        description = "Retorna la información completa de un producto específico. " +
                "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>",
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":true,\"message\":\"Producto encontrado\",\"data\":{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"laptop hp\",\"category\":{\"id\":\"123e4567-e89b-12d3-a456-426614174001\",\"name\":\"Electrónica\"},\"price\":999.99,\"photoUrl\":null,\"currentStock\":10,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"statusCode\":404,\"errorCode\":\"NOT_FOUND\",\"message\":\"No se encontró el producto con ID: 123e4567-e89b-12d3-a456-426614174000\",\"details\":[\"El producto especificado no existe en el sistema\"],\"timestamp\":\"2025-11-26T15:10:38.908929300Z\",\"path\":\"/api/products/123e4567-e89b-12d3-a456-426614174000\"}"
                        )
                )
        )
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetProductByIdDoc {
}
