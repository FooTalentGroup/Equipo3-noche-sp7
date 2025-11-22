package com.stockia.stockia.documentation.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Swagger documentation for PUT /api/users.
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Actualizar usuario",
        description = """
        Actualiza la información de un usuario existente identificado por su ID. \
        Accesible solo para usuarios autorizados con rol <strong>ADMIN.</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuario actualizado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "success": true,
                          "message": "Usuario actualizado exitosamente.",
                          "data": {
                            "email": "juan.perez@example.com",
                            "role": "ADMIN"
                          }
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida: datos malformados o violaciones de validación",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 400,
                          "errorCode": "VALIDATION_ERROR",
                          "message": "Error de validación",
                          "details": "email: no puede estar vacío",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/{id}"
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
                          "path": "/api/users/{id}"
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
                          "path": "/api/users/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 404,
                          "errorCode": "NOT_FOUND",
                          "message": "Usuario no encontrado con id: ...",
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "El nombre de usuario ingresado ya esta en uso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 409,
                          "errorCode": "CONFLICT",
                          "message": "El nombre de usuario ya está en uso",
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/{id}"
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
                          "details": "NullPointerException ...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/{id}"
                        }
                        """)
                )
        )
})
public @interface UpdateUserEndpointDoc {}
