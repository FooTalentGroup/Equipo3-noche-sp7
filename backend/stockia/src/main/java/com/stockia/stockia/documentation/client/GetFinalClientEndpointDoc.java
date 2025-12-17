package com.stockia.stockia.documentation.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener cliente Consumidor Final",
        description = """
            Devuelve los datos del cliente Consumidor Final del sistema. \s
            • Este cliente es utilizado por defecto para ventas sin cliente registrado. \s
            • Solo usuarios con rol <strong>ADMIN</strong> o <strong>MANAGER</strong> pueden acceder.
            """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Cliente Consumidor Final encontrado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                            {
                              "success": true,
                              "message": "Cliente Consumidor Final encontrado",
                              "data": {
                                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                "name": "Consumidor Final",
                                "email": "consumidor-final@stockia.com",
                                "phone": "+0000000000",
                                "isFrequent": false,
                                "clientStatus": "ACTIVE"
                              }
                            }
                            """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente Consumidor Final no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                            {
                              "statusCode": 404,
                              "errorCode": "NOT_FOUND",
                              "message": "Cliente Consumidor Final no encontrado",
                              "details": "...",
                              "timestamp": "2025-11-10T20:12:00Z",
                              "path": "/api/clients/final"
                            }
                            """)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado por falta de permisos",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                            {
                              "statusCode": 403,
                              "errorCode": "FORBIDDEN",
                              "message": "Acceso denegado",
                              "details": "...",
                              "timestamp": "2025-11-10T20:12:00Z",
                              "path": "/api/clients/final"
                            }
                            """)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado (token ausente o inválido)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                            {
                              "statusCode": 401,
                              "errorCode": "AUTH_ERROR",
                              "message": "Acceso no autorizado",
                              "details": "...",
                              "timestamp": "2025-11-10T20:12:00Z",
                              "path": "/api/clients/final"
                            }
                            """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                            {
                              "statusCode": 500,
                              "errorCode": "INTERNAL_SERVER_ERROR",
                              "message": "Error inesperado",
                              "details": "...",
                              "timestamp": "2025-11-10T20:12:00Z",
                              "path": "/api/clients/final"
                            }
                            """)
                )
        )
})
public @interface GetFinalClientEndpointDoc {
}
