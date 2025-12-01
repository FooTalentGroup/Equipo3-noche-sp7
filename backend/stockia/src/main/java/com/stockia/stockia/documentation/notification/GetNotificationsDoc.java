package com.stockia.stockia.documentation.notification;

import com.stockia.stockia.documentation.common.SecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: GET /api/notifications
 * Lista notificaciones con estado de lectura personalizado por usuario.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Listar notificaciones con paginación", description = "Obtiene todas las notificaciones del sistema con el estado de lectura específico del usuario autenticado. "
                +
                "Soporta filtrado por tipo, estado de lectura y producto relacionado. " +
                "Incluye paginación y ordenamiento configurable (por defecto: 20 elementos, ordenados por fecha descendente). "
                +
                "<strong>Accesible para todos los usuarios autenticados.</strong>", security = @SecurityRequirement(name = "bearer-key"), parameters = {
                                @Parameter(name = "type", description = "Filtrar por tipo de notificación (LOW_STOCK, OUT_OF_STOCK, etc.)", example = "LOW_STOCK"),
                                @Parameter(name = "isRead", description = "Filtrar por estado de lectura (true = leídas, false = no leídas)", example = "false"),
                                @Parameter(name = "referenceId", description = "Filtrar por ID del producto relacionado", example = "550e8400-e29b-41d4-a716-446655440001"),
                                @Parameter(name = "page", description = "Número de página (inicia en 0)", example = "0"),
                                @Parameter(name = "size", description = "Cantidad de elementos por página", example = "20"),
                                @Parameter(name = "sort", description = "Campo y dirección de ordenamiento", example = "createdAt,desc")
                })
@ApiResponses({
                @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"5 notificación(es) encontrada(s)\",\"data\":{\"content\":[{\"id\":\"550e8400-e29b-41d4-a716-446655440000\",\"message\":\"Stock bajo: El producto 'Laptop HP' tiene 3 unidades (mínimo: 5).\",\"type\":\"LOW_STOCK\",\"referenceId\":\"123e4567-e89b-12d3-a456-426614174001\",\"referenceName\":\"Laptop HP\",\"isRead\":false,\"readAt\":null,\"createdAt\":\"2025-12-01T09:15:00\"}],\"totalElements\":5,\"totalPages\":1,\"number\":0,\"size\":20}}"))),
                @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(examples = @ExampleObject(value = "{\"statusCode\":401,\"errorCode\":\"AUTH_ERROR\",\"message\":\"Token inválido o ausente\",\"details\":[\"Se requiere autenticación completa para acceder a este recurso.\"],\"timestamp\":\"2025-12-01T15:10:38Z\",\"path\":\"/api/notifications\"}"))),
                @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(examples = @ExampleObject(value = "{\"statusCode\":403,\"errorCode\":\"FORBIDDEN\",\"message\":\"Acceso denegado. No tienes permisos para realizar esta acción\",\"details\":[\"No tienes los permisos necesarios para realizar esta operación\"],\"timestamp\":\"2025-12-01T15:10:38Z\",\"path\":\"/api/notifications\"}")))
})
@SecurityResponses.Unauthorized
public @interface GetNotificationsDoc {
}
