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
 * Documentación del endpoint: GET /api/products
 * Lista todos los productos activos y disponibles del inventario.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Listar productos activos",
    description = "Retorna todos los productos activos y disponibles. " +
                  "Opcionalmente puede filtrar por búsqueda de texto (q) y/o categoría (categoryId). " +
                  "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida exitosamente",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":true,\"message\":\"3 producto(s) encontrado(s)\",\"data\":[{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"currentStock\":10,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}]}"
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
        description = "Acceso denegado - Se requiere rol ADMIN o MANAGER",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"Acceso denegado. No tienes permisos para realizar esta acción\",\"data\":null}"
            )
        )
    )
})
public @interface GetAllProductsDoc {
}

