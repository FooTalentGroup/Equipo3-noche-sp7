package com.stockia.stockia.documentation.product;
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
 * Documentación del endpoint: GET /api/products/deleted
 * Obtiene el historial de productos eliminados (soft delete).
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Listar productos eliminados",
    description = "Retorna el historial de productos eliminados (soft delete). " +
                  "<strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos eliminados",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":true,\"message\":\"2 producto(s) eliminado(s) encontrado(s)\",\"data\":[{\"id\":5,\"name\":\"mouse inalambrico\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":29.99,\"isAvailable\":false,\"deletedAt\":\"2025-11-21T15:20:00\"}]}"
            )
        )
    )
})
@SecurityResponses.RequiresAdmin
public @interface GetDeletedProductsDoc {
}

