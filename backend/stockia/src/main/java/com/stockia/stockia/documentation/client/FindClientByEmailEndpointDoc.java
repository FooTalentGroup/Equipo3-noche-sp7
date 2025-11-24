package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/clients/buscar?email={email}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Buscar cliente por email", description = "Busca y retorna un cliente mediante su dirección de correo electrónico.", parameters = @Parameter(name = "email", description = "Email del cliente", required = true))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "id": "550e8400-e29b-41d4-a716-446655440000",
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
                      "path": "/api/clients/buscar"
                    }
                """)))
})
public @interface FindClientByEmailEndpointDoc {
}
