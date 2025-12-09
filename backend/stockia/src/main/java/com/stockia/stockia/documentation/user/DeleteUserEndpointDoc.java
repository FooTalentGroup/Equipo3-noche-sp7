package com.stockia.stockia.documentation.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Swagger documentation for DELETE /api/users.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Eliminar Usuario",
        description = """
        Elimina un usuario de manera logica del sistema por su ID único. \s
        Solo usuarios con rol <strong>ADMIN</strong> pueden realizar esta operación.
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Usuario eliminado correctamente",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado (token ausente o inválido)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 401,
                          "errorCode": "UNAUTHORIZED",
                          "message": "Acceso no autorizado",
                          "details": "Token inválido o expirado",
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
                          "details": "El usuario no tiene permisos para eliminar el usuario",
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
                          "message": "No se encontró usuario con ID: {id}",
                          "details": "No existe un usuario con el ID proporcionado",
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
                          "details": "NullPointerException at line ...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/{id}"
                        }
                        """)
                )
        )
})
public @interface DeleteUserEndpointDoc {}