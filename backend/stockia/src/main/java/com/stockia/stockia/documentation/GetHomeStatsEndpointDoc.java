package com.stockia.stockia.documentation;

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
        summary = "Obtener estadísticas del panel de inicio",
        description = """
        Retorna un conjunto de estadísticas generales para mostrar en la pantalla Home.
        
        Incluye:
        - Cantidad de productos con stock bajo.
        - Cantidad de productos activos.
        - Ventas realizadas en la semana.
        - Cantidad de clientes siendo atendidos.
        
        <strong>Requiere autenticación mediante Bearer Token.</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Estadísticas obtenidas exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                {
                  "lowStockProducts": 7,
                  "activeProducts": 128,
                  "salesThisWeek": 42,
                  "clientsBeingServed": 5
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado (token ausente o inválido).",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                {
                  "statusCode": 401,
                  "errorCode": "AUTH_ERROR",
                  "message": "Acceso no autorizado",
                  "details": "...",
                  "timestamp": "2025-11-10T20:12:00Z",
                  "path": "/api/home/stats"
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado. Usuario sin permisos.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                {
                  "statusCode": 403,
                  "errorCode": "FORBIDDEN",
                  "message": "Acceso denegado",
                  "details": "...",
                  "timestamp": "2025-11-10T20:12:00Z",
                  "path": "/api/home/stats"
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
                  "path": "/api/home/stats"
                }
            """)
                )
        )
})
public @interface GetHomeStatsEndpointDoc {}
