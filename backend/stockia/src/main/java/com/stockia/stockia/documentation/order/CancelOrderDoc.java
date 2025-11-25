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
 * Documentación del endpoint: PATCH /api/orders/{id}/cancel
 * Cancela una orden y restaura el stock.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Cancelar orden", description = "Anula una orden cambiando su estado a CANCELLED. " +
        "Restaura automáticamente el stock de todos los productos. " +
        "Crea movimientos de inventario inversos (IN). " +
        "Registra el motivo de cancelación y el usuario que ejecutó la acción. " +
        "Si el pago estaba PAID, se marca como REFUNDED. " +
        "Solo se pueden cancelar órdenes en estado PENDING o CONFIRMED. " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden cancelada exitosamente y stock restaurado", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Orden cancelada exitosamente\",\"data\":{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"status\":\"CANCELLED\",\"cancelReason\":\"Cliente solicitó devolución\",\"paymentStatus\":\"REFUNDED\"}}"))),
        @ApiResponse(responseCode = "400", description = "Datos de cancelación inválidos", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"El motivo de cancelación es obligatorio\",\"data\":null}"))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró la orden con ID: 770e8400-e29b-41d4-a716-446655440002\",\"data\":null}"))),
        @ApiResponse(responseCode = "409", description = "Estado inválido para cancelación", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se puede cancelar una orden en estado DELIVERED\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface CancelOrderDoc {
}
