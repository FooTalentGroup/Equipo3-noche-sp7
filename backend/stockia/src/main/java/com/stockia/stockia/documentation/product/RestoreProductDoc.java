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

/**
 * Documentación del endpoint: POST /api/products/{id}/restore
 * Restaura un producto previamente eliminado.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Restaurar producto eliminado",
    description = "Recupera un producto previamente eliminado (soft delete), restaurando su disponibilidad. " +
                  "<strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto restaurado exitosamente",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto restaurado exitosamente\",\"data\":{\"id\":5,\"name\":\"mouse inalambrico\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":29.99,\"isAvailable\":true}}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"No se encontró el producto con ID: 999\",\"data\":null}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "El producto no está eliminado",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"El producto no está eliminado\",\"data\":null}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "401",
        description = "No autorizado - Token ausente o inválido",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"Acceso no autorizado. Token inválido o ausente\",\"data\":null}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "403",
        description = "Acceso denegado - Se requiere rol ADMIN",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"Acceso denegado. No tienes permisos para realizar esta acción\",\"data\":null}"
            )
        )
    )
})
public @interface RestoreProductDoc {
}

