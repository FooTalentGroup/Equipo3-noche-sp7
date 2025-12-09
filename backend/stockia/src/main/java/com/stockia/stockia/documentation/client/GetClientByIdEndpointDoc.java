package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/clients/{id}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Obtener cliente por ID",
        description = "Retorna los datos de un cliente específico mediante su ID.",
        parameters = @Parameter(name = "id", description = "ID del cliente",required = true),
        security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "id": "550e8400- e29b-41d4-a716-446655440000",
                      "name": "Juan Pérez",
                      "email": "juan.perez@email.com",
                      "phone": "555-1234",
                      "isFrequent": true
                    }
                """))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 404,
                      "message": "Client no encontrado",
                      "errorCode": "CLIENT_NOT_FOUND",
                      "details": [],
                      "path": "/api/clients/550e8400-e29b-41d4-a716-446655440000"
                    }
                """)))
})
public @interface GetClientByIdEndpointDoc {
}
