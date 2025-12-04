package com.stockia.stockia.documentation.order;

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
 * Documentación del endpoint: GET /api/orders/status/{status}
 * Filtra órdenes por su estado.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Filtrar órdenes por estado", description = "Retorna todas las órdenes que tienen el estado especificado. "
        +
        "Estados válidos: PENDING, CONFIRMED, DELIVERED, CANCELLED. " +
        "Ordenadas por fecha descendente (más recientes primero). " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de órdenes filtradas por estado", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"5 orden(es) encontrada(s)\",\"data\":[{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"status\":\"CONFIRMED\"}]}"))),
        @ApiResponse(responseCode = "400", description = "Estado inválido", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Estado de orden inválido\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetOrdersByStatusDoc {
}
