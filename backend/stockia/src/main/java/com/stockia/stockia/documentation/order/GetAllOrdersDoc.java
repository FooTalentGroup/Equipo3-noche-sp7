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
 * Documentación del endpoint: GET /api/orders
 * Lista todas las órdenes del sistema.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Listar todas las órdenes", description = "Retorna todas las órdenes registradas en el sistema ordenadas por fecha descendente (más recientes primero). "
        +
        "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente", content = @Content(examples = @ExampleObject(value = "{\"success\":true,\"message\":\"15 orden(es) encontrada(s)\",\"data\":[{\"id\":\"770e8400-e29b-41d4-a716-446655440002\",\"orderNumber\":\"ORD-20251124-0001\",\"status\":\"CONFIRMED\"},{\"id\":\"770e8400-e29b-41d4-a716-446655440003\",\"orderNumber\":\"ORD-20251124-0002\",\"status\":\"DELIVERED\"}]}")))
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetAllOrdersDoc {
}
