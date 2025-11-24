package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/clients.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Registrar nuevo cliente",
        description = "Registra un nuevo cliente en el sistema. El email y teléfono deben ser únicos.",
        security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "id": "550e8400-e29b-41d4-a716-446655440000",
                      "name": "Juan Pérez",
                      "email": "juan.perez@email.com",
                      "phone": "555-1234",
                      "isFrequent": false
                    }
                """))),
        @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 400,
                      "message": "Falló la validación de los campos",
                      "errorCode": "VALIDATION_ERROR",
                      "details": [
                        "email: El email es requerido",
                        "phone: El teléfono es requerido"
                      ],
                      "path": "/api/clients"
                    }
                """))),
        @ApiResponse(responseCode = "409", description = "Conflicto - Cliente duplicado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 409,
                      "message": "El cliente ya está registrado con ese correo o teléfono",
                      "errorCode": "CLIENT_DUPLICATED",
                      "details": [],
                      "path": "/api/clients"
                    }
                """)))
})
public @interface RegisterClientEndpointDoc {
}
