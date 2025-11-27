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
@Operation(summary = "Registrar nuevo cliente", description = "Registra un nuevo cliente en el sistema. El email y teléfono deben ser únicos. "
    +
    "El campo isFrequent indica si el cliente es frecuente (true) o no (false).", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
        {
          "success": true,
          "data": {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "name": "María García López",
            "email": "maria.garcia@example.com",
            "phone": "+5491123456789",
            "isFrequent": true
          },
          "message": "Cliente registrado exitosamente"
        }
        """))),
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", schema = @Schema(example = """
        {
          "statusCode": 400,
          "message": "Falló la validación de los campos",
          "errorCode": "VALIDATION_ERROR",
          "details": [
            "name: El nombre es obligatorio",
            "email: El formato del correo electrónico es inválido",
            "phone: El teléfono debe comenzar con código de país (+XX) seguido de 8-12 dígitos. Ejemplo: +54911234567",
            "isFrequent: Debe indicar si es cliente frecuente"
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
