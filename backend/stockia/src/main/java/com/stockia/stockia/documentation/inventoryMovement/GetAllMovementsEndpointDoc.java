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
    summary = "Obtener todos los movimientos de inventario",
    description = """
        Retorna la lista paginada de movimientos de inventario.
        Tambien funciona con filtros.
        
        Accesible para usuarios Autenticados.
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista paginada de movimientos de inventario obtenido exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                {
                  "content": [
                    {
                      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                      "productId": "123e4567-e89b-12d3-a456-426614174000",
                      "productName": "Manzana",
                      "movementType": "ADJUSTMENT",
                      "quantity": "80",
                      "reason": "...",
                      "userId": "3fa85f64-5717-4562-b3fc-2c963f66af01",
                      ...
                    },
                    {
                      "id": "3fa85f64-5717-4562-b3fc-2c963f66af01",
                      "productId": "3fa32420-5717-5717-b3fc-b3fc3f66b3fc",
                      "productName": "Sandwich",
                      "movementType": "IN",
                      "quantity": "4",
                      "reason": "...",
                      "userId": "3fa85f64-5717-4562-b3fc-2c963f66af01",
                      ...
                    }
                  ],
                  "pageable": {
                    "pageNumber": 0,
                    "pageSize": 10,
                    "sort": {
                      "sorted": true,
                      "unsorted": false,
                      "empty": false
                    }
                  },
                  "totalPages": 5,
                  "totalElements": 50,
                  "last": false,
                  "size": 10,
                  "number": 0,
                  "sort": {
                    "sorted": true,
                    "unsorted": false,
                    "empty": false
                  },
                  "first": true,
                  "numberOfElements": 10,
                  "empty": false
                }
                """)
                )
        ),
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
                  "path": "/api/inventory-movements"
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
                  "path": "/api/inventory-movements"
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
                          "path": "/api/inventory-movements"
                        }
                        """))),

})
public @interface GetAllMovementsEndpointDoc {}
