package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "05 - Clientes")
@Operation(
        summary = "Consultar historial de compras de cliente",
        description = "Obtiene el historial completo de compras/órdenes realizadas por un cliente específico, ordenadas por fecha descendente (más recientes primero).",
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
    @ApiResponse(responseCode = "401", description = "No autorizado"),
    @ApiResponse(responseCode = "403", description = "Acceso prohibido")
})
public @interface GetClientPurchaseHistoryDoc {
    
}
