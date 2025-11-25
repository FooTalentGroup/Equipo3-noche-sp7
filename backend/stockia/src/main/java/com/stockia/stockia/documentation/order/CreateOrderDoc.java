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
 * Documentación del endpoint: POST /api/orders
 * Crea una nueva orden de venta con validación de stock.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Crear nueva orden de venta", description = "Registra una nueva orden en estado PENDING validando stock disponible y bloqueándolo inmediatamente. "
        +
        "Se genera automáticamente un número de orden único con formato ORD-YYYYMMDD-XXXX. " +
        "El usuario responsable se obtiene del token de autenticación. " +
        "Se crean movimientos de inventario automáticamente. " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Orden creada exitosamente con stock reservado", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Orden creada exitosamente\",\"data\":{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"customerId\":\"550e8400-e29b-41d4-a716-446655440000\",\"customerName\":\"Juan Pérez\",\"status\":\"PENDING\",\"subtotal\":301.00,\"discountAmount\":10.00,\"totalAmount\":291.00,\"paymentMethod\":\"CASH\",\"paymentStatus\":\"PENDING\"}}"))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o la orden no tiene productos", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"La orden debe contener al menos un producto\",\"data\":null}"))),
        @ApiResponse(responseCode = "404", description = "Cliente o producto no encontrado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró el cliente con ID: 550e8400-e29b-41d4-a716-446655440000\",\"data\":null}"))),
        @ApiResponse(responseCode = "409", description = "Stock insuficiente para uno o más productos", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Stock insuficiente para el producto. Stock actual: 1, cantidad requerida: 2\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface CreateOrderDoc {
}
