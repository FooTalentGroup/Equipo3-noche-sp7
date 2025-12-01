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
 * Documentación del endpoint: DELETE /api/notifications/{id}
 * Elimina una notificación del sistema.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Eliminar notificación (soft delete)", description = "Elimina una notificación del sistema mediante soft delete. "
        +
        "La notificación se marca como eliminada pero se conserva en la base de datos para auditoría. " +
        "La eliminación afecta a TODOS los usuarios (la notificación desaparece para todos). " +
        "<strong>Solo accesible para usuarios con rol ADMIN.</strong>", security = @SecurityRequirement(name = "bearer-key"), parameters = {
                @Parameter(name = "id", description = "ID de la notificación a eliminar", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
        })
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación eliminada exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Notificación eliminada exitosamente\",\"data\":null}"))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Token inválido o expirado\",\"data\":null}"))),
        @ApiResponse(responseCode = "403", description = "No autorizado - requiere rol ADMIN", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Acceso denegado. Se requiere rol de administrador\",\"data\":null}"))),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Notificación no encontrada con ID: 550e8400-e29b-41d4-a716-446655440000\",\"data\":null}")))
})
@SecurityResponses.RequiresAdmin
public @interface DeleteNotificationDoc {
}
