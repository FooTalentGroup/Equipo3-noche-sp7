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
 * Documentación del endpoint: GET /api/notifications/unread-count
 * Obtiene el contador de notificaciones no leídas del usuario.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Obtener contador de notificaciones no leídas", description = "Retorna la cantidad de notificaciones que el usuario autenticado aún no ha marcado como leídas. "
        +
        "Útil para mostrar badges o indicadores en la interfaz. " +
        "<strong>Accesible para todos los usuarios autenticados.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contador obtenido exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Tienes 3 notificación(es) sin leer\",\"data\":3}"))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Token inválido o expirado\",\"data\":null}"))),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Acceso denegado\",\"data\":null}")))
})
@SecurityResponses.Unauthorized
public @interface GetUnreadCountDoc {
}
