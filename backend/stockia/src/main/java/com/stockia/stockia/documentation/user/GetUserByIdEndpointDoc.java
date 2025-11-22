package com.stockia.stockia.documentation.user;

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
    summary = "Obtener usuario por ID",
    description = """
        Devuelve los datos de un usuario específico. \s
        • El <strong>ADMIN</strong> puede ver todos los usuarios. \s
        • Usuarios con rol <strong>MANAGER</strong> solo pueden ver su propio perfil.
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Usuario encontrado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = """
                {
                  "success": true,
                  "message": "Usuario encontrado exitosamente.",
                  "data": {
                    "uuid": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                    "email": "juan.perez@example.com",
                    "role": "MANAGER"
                  }
                }
                """)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado",
        content =
        @Content(
            mediaType = "application/json",
            schema =
            @Schema(
                example =
                    """
                        {
                          "statusCode": 404,
                          "errorCode": "NOT_FOUND",
                          "message": "Usuario no encontrado con id: ...",
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/getUserById/..."
                        }
                        """))),
    @ApiResponse(responseCode = "403",
        description = "Acceso denegado por falta de permisos. Usuario con rol no autorizado.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = """
                {
                  "statusCode": 403,
                  "errorCode": "FORBIDDEN",
                  "message": "Acceso denegado",
                  "details": "...",
                  "timestamp": "2025-11-10T20:12:00Z",
                  "path": "/api/users/getUserById/..."
                }
            """)
        )
    ),
    @ApiResponse(responseCode = "401", description = "No autorizado (token ausente o inválido).",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = """
                {
                  "statusCode": 401,
                  "errorCode": "AUTH_ERROR",
                  "message": "Acceso no autorizado",
                  "details": "...",
                  "timestamp": "2025-11-10T20:12:00Z",
                  "path": "/api/users/getUserById/..."
                }
            """)
        )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content =
        @Content(
            mediaType = "application/json",
            schema =
            @Schema(
                example =
                    """
                        {
                          "statusCode": 500,
                          "errorCode": "INTERNAL_SERVER_ERROR",
                          "message": "Error inesperado",
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/users/getUserById/..."
                        }
                        """))),
})
public @interface GetUserByIdEndpointDoc {
}
