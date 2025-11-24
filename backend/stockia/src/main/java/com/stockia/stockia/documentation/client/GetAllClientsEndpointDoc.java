package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/clients.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Obtener todos los clientes",
        description = "Retorna la lista completa de todos los clientes registrados en el sistema.",
        security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
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
                        "name": "María González",
                        "email": "maria.gonzalez@email.com",
                        "phone": "555-5678",
                        "isFrequent": false
                      }
                    ]
                """)))
})
public @interface GetAllClientsEndpointDoc {
}
