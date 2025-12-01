package com.stockia.stockia.documentation.notification;

import com.stockia.stockia.documentation.common.SecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
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
 * Documentación del endpoint: PUT /api/notifications/read-all
 * Marca todas las notificaciones como leídas.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Marcar todas las notificaciones como leídas", description = "Marca todas las notificaciones activas como leídas para el usuario autenticado. "
        +
        "Solo afecta las notificaciones que el usuario aún no ha leído. " +
        "Útil para implementar botón 'Marcar todo como leído'. " +
        "<strong>Accesible para todos los usuarios autenticados.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Todas las notificaciones marcadas como leídas", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Todas las notificaciones han sido marcadas como leídas\",\"data\":null}"))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Token inválido o expirado\",\"data\":null}"))),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Acceso denegado\",\"data\":null}")))
})
@SecurityResponses.Unauthorized
public @interface MarkAllAsReadDoc {
}
