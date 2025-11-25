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
 * Documentación del endpoint: GET /api/orders/{id}
 * Obtiene los detalles completos de una orden por su ID.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Obtener orden por ID", description = "Retorna toda la información de una orden incluyendo: datos del cliente, usuario responsable, "
        +
        "items con detalles de productos, totales, estado de pago y fechas. " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden encontrada exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Orden encontrada\",\"data\":{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"status\":\"CONFIRMED\",\"subtotal\":301.00,\"totalAmount\":291.00,\"paymentMethod\":\"CASH\",\"paymentStatus\":\"PAID\"}}"))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró la orden con ID: 770e8400-e29b-41d4-a716-446655440002\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetOrderByIdDoc {
}
