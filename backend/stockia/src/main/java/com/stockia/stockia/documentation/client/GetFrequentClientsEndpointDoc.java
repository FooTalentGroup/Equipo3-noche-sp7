package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/clients/frecuentes.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Obtener clientes frecuentes",
        description = "Retorna la lista de clientes marcados como frecuentes (isFrequent = true).",
        security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes frecuentes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    [
                      {
                        "id": "550e8400-e29b-41d4-a716-446655440000",
                        "name": "Juan Pérez",
                        "email": "juan.perez@email.com",
                        "phone": "555-1234",
                        "isFrequent": true
                      },
                      {
                        "id": "550e8400-e29b-41d4-a716-446655440001",
                        "name": "Pedro García",
                        "email": "pedro.garcia@email.com",
                        "phone": "555-9999",
                        "isFrequent": true
                      }
                    ]
                """)))
})
public @interface GetFrequentClientsEndpointDoc {
}
