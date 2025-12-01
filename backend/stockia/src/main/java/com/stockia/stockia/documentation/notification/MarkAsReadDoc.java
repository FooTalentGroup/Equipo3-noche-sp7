package com.stockia.stockia.documentation.notification;

import com.stockia.stockia.documentation.common.SecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: PUT /api/notifications/{id}/read
 * Marca una notificación como leída.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Marcar notificación como leída", description = "Marca una notificación específica como leída para el usuario autenticado. "
        +
        "La operación es idempotente: si ya está marcada como leída, retorna el mismo resultado sin error. " +
        "El estado de lectura es independiente para cada usuario. " +
        "<strong>Accesible para todos los usuarios autenticados.</strong>", security = @SecurityRequirement(name = "bearer-key"), parameters = {
                @Parameter(name = "id", description = "ID de la notificación a marcar como leída", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
        })
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Notificación marcada como leída\",\"data\":{\"id\":\"550e8400-e29b-41d4-a716-446655440000\",\"message\":\"Stock bajo: El producto 'Laptop HP' tiene 3 unidades (mínimo: 5).\",\"type\":\"LOW_STOCK\",\"referenceId\":\"123e4567-e89b-12d3-a456-426614174001\",\"referenceName\":\"Laptop HP\",\"isRead\":true,\"readAt\":\"2025-12-01T10:30:00\",\"createdAt\":\"2025-12-01T09:15:00\"}}"))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Token inválido o expirado\",\"data\":null}"))),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Notificación no encontrada con ID: 550e8400-e29b-41d4-a716-446655440000\",\"data\":null}")))
})
@SecurityResponses.Unauthorized
public @interface MarkAsReadDoc {
}
