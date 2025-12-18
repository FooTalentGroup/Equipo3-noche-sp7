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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Editar orden de venta", description = "Permite editar los items de una orden existente en estado PENDING. "
                +
                "<strong>Restricciones:</strong> " +
                "Solo el usuario que creó la orden puede editarla. " +
                "Solo se pueden editar órdenes en estado PENDING. " +
                "Se verifica stock disponible para los nuevos items. " +
                "El stock de los items anteriores se restaura automáticamente. " +
                "Se crean movimientos de inventario (IN para items anteriores, OUT para nuevos items). " +
                "<strong>Solo accesible para usuarios con rol ADMIN, MANAGER o EMPLOYEE.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
                @ApiResponse(responseCode = "200", description = "Orden editada exitosamente con stock actualizado", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Orden editada exitosamente\",\"data\":{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251217-0001\",\"customerId\":\"550e8400-e29b-41d4-a716-446655440000\",\"customerName\":\"Juan Pérez\",\"status\":\"PENDING\",\"subtotal\":450.00,\"discountAmount\":10.00,\"totalAmount\":440.00,\"paymentMethod\":\"CASH\",\"paymentStatus\":\"PENDING\"}}"))),
                @ApiResponse(responseCode = "400", description = "Estado inválido - la orden no está en estado PENDING", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se puede editar una orden en estado CONFIRMED\",\"data\":null}"))),
                @ApiResponse(responseCode = "401", description = "Usuario no autorizado - no es el creador de la orden", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No tiene permisos para editar esta orden. Solo el usuario que la creó puede editarla.\",\"data\":null}"))),
                @ApiResponse(responseCode = "404", description = "Orden o producto no encontrado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró la orden con ID: 770e8400-e29b-41d4-a716-446655440002\",\"data\":null}"))),
                @ApiResponse(responseCode = "409", description = "Stock insuficiente para uno o más productos", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Stock insuficiente para el producto 'Coca Cola 2L'. Stock actual: 5, cantidad requerida: 10\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManagerOrEmployee
public @interface EditOrderDoc {
}
