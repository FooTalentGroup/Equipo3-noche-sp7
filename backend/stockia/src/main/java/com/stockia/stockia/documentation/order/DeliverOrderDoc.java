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
 * Documentación del endpoint: PATCH /api/orders/{id}/deliver
 * Marca una orden como entregada.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Marcar orden como entregada", description = "Cambia el estado de una orden de CONFIRMED a DELIVERED. "
        +
        "Registra la fecha y hora de entrega. " +
        "Solo se pueden marcar como entregadas órdenes en estado CONFIRMED. " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden marcada como entregada exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Orden marcada como entregada exitosamente\",\"data\":{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"status\":\"DELIVERED\",\"deliveredDate\":\"2025-11-24T17:00:00\"}}"))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró la orden con ID: 770e8400-e29b-41d4-a716-446655440002\",\"data\":null}"))),
        @ApiResponse(responseCode = "409", description = "Estado inválido para marcar como entregada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se puede marcar como entregada una orden en estado PENDING\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface DeliverOrderDoc {
}
