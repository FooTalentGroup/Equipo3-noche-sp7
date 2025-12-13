package com.stockia.stockia.documentation.client;

import com.stockia.stockia.documentation.common.SecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint DELETE /api/clients/{id} - Baja lógica de cliente.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Dar de baja un cliente",
        description = """
                Marca el cliente como INACTIVE sin eliminarlo físicamente.
                El cliente se conserva para historial de órdenes.
                
                <strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>
                """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Cliente dado de baja correctamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "message": "Cliente dado de baja correctamente",
                                  "data": null
                                }
                                """)
                )
        ),

        @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "statusCode": 404,
                                  "errorCode": "CLIENT_NOT_FOUND",
                                  "message": "Cliente no encontrado",
                                  "details": [
                                    "No se encontró el cliente con ID: 123e4567-e89b-12d3-a456-426614174001"
                                  ],
                                  "timestamp": "2025-12-12T18:42:10.123Z",
                                  "path": "/api/clients/123e4567-e89b-12d3-a456-426614174001"
                                }
                                """)
                )
        ),

        @ApiResponse(
                responseCode = "409",
                description = "El cliente ya se encuentra inactivo",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "statusCode": 409,
                                  "errorCode": "CLIENT_ALREADY_INACTIVE",
                                  "message": "El cliente ya está dado de baja",
                                  "details": [
                                    "El cliente ya se encuentra en estado INACTIVE"
                                  ],
                                  "timestamp": "2025-12-12T18:42:10.123Z",
                                  "path": "/api/clients/123e4567-e89b-12d3-a456-426614174001"
                                }
                                """)
                )
        )
})
@SecurityResponses.RequiresAdminOrManager
public @interface DeleteClientEndpointDoc {
}
