package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: GET /api/products/management
 * Endpoint avanzado para administradores con múltiples filtros.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Gestión avanzada de productos",
    description = "Endpoint para administradores que permite aplicar múltiples filtros. " +
                  "Filtros disponibles: " +
                  "- includeInactive: incluye productos con isAvailable=false (por defecto false) " +
                  "- lowStock: solo productos con stock bajo (por defecto false) " +
                  "- q: búsqueda por texto en el nombre " +
                  "- categoryId: filtrar por categoría específica"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida exitosamente",
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\"success\":true,\"message\":\"2 producto(s) encontrado(s)\",\"data\":[{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"currentStock\":2,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":true}]}"
            )
        )
    )
})
public @interface GetProductsManagementDoc {
}

