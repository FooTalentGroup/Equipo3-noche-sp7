package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Documentación Swagger para PUT /api/clients/{id}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Actualizar cliente existente", description = "Actualiza los datos de un cliente existente. Si se modifica el email o teléfono, "
    +
    "se valida que no estén asociados a otro cliente.", security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
        {
          "success": true,
          "data": {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "name": "Juan Pérez Actualizado",
            "email": "juan.perez.nuevo@email.com",
            "phone": "+5491187654321",
            "isFrequent": true
          },
          "message": "Cliente actualizado exitosamente"
        }
        """))),
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", schema = @Schema(example = """
        {
          "statusCode": 400,
          "message": "Falló la validación de los campos",
          "errorCode": "VALIDATION_ERROR",
          "details": [
            "email: El formato del correo electrónico es inválido",
            "phone: El teléfono debe comenzar con código de país (+XX) seguido de 8-12 dígitos"
          ],
          "path": "/api/clients/550e8400-e29b-41d4-a716-446655440000"
        }
        """))),
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
        {
          "statusCode": 404,
          "message": "Cliente no encontrado con ID: 550e8400-e29b-41d4-a716-446655440000",
          "errorCode": "CLIENT_NOT_FOUND",
          "details": [],
          "path": "/api/clients/550e8400-e29b-41d4-a716-446655440000"
        }
        """))),
    @ApiResponse(responseCode = "409", description = "Conflicto - Email o teléfono duplicado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
        {
          "statusCode": 409,
          "message": "El email ya está asociado a otro cliente",
          "errorCode": "CLIENT_DUPLICATED",
          "details": [],
          "path": "/api/clients/550e8400-e29b-41d4-a716-446655440000"
        }
        """)))
})
public @interface UpdateClientEndpointDoc {
}
