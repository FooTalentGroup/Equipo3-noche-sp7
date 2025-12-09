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
 * Documentación del endpoint: PATCH /api/orders/{id}/confirm
 * Confirma una orden en estado PENDING.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Confirmar orden", description = "Cambia el estado de una orden de PENDING a CONFIRMED. " +
        "El stock ya fue reservado en la creación de la orden. " +
        "Actualiza el estado del pago a PAID. " +
        "Solo se pueden confirmar órdenes en estado PENDING. " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden confirmada exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Orden confirmada exitosamente\",\"data\":{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"status\":\"CONFIRMED\",\"paymentStatus\":\"PAID\"}}"))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró la orden con ID: 770e8400-e29b-41d4-a716-446655440002\",\"data\":null}"))),
        @ApiResponse(responseCode = "409", description = "Estado inválido para confirmación", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se puede confirmar una orden en estado CONFIRMED\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface ConfirmOrderDoc {
}
