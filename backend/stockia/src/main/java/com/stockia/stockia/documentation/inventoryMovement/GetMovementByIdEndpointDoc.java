package com.stockia.stockia.documentation.inventoryMovement;

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
    summary = "Obtener movimiento de inventario por ID",
    description = """
             Devuelve los detalles de un movimiento de inventario específico mediante su ID. \\s
             Accesible para usuarios Autenticados.
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Movimiento de inventario encontrado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = """
                {
                  "success": true,
                  "message": "Movimiento de inventario encontrado exitosamente.",
                  "data": {
                    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                    "productId": "3fa32420-5717-5717-b3fc-b3fc3f66b3fc",
                    "productName": "",
                    "movementType": "",
                    "quantity": "",
                    "reason": "",
                    "userId": "",
                    ...
                  }
                }
                """)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Movimiento de inventario no encontrado",
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
                          "message": "Movimiento de inventario no encontrado con id: ...",
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/inventory-movements/{id}"
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
                  "path": "/api/inventory-movements/{id}"
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
                  "path": "/api/inventory-movements/{id}"
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
                          "path": "/api/inventory-movements/{id}"
                        }
                        """))),
})
public @interface GetMovementByIdEndpointDoc {
}
