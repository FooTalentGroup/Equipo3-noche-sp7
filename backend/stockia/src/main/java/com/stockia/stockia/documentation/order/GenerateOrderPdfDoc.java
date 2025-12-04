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
 * Documentación del endpoint: GET /api/orders/{id}/pdf
 * Genera y descarga el comprobante PDF de una orden.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Generar comprobante PDF", description = "Genera un documento PDF con el comprobante de venta incluyendo: "
        +
        "información de la orden, datos del cliente, detalle de productos, totales y método de pago. " +
        "El archivo se descarga automáticamente con nombre orden_{id}.pdf. " +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "PDF generado exitosamente y descargado"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"No se encontró la orden con ID: 770e8400-e29b-41d4-a716-446655440002\",\"data\":null}"))),
        @ApiResponse(responseCode = "500", description = "Error al generar el PDF", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Error al generar el PDF del comprobante\",\"data\":null}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface GenerateOrderPdfDoc {
}
