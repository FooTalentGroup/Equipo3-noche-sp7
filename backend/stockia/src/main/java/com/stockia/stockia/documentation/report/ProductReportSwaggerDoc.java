package com.stockia.stockia.documentation.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ProductReportSwaggerDoc {

        @Target({ ElementType.TYPE })
        @Retention(RetentionPolicy.RUNTIME)
        @Tag(name = "09 - Reportes de Productos", description = "Endpoints para generar reportes y estadísticas de productos")
        public @interface ProductReportControllerTag {
        }

        @Target({ ElementType.METHOD })
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(summary = "Obtener reporte de productos más vendidos", description = """
                        Genera un reporte de los productos más vendidos en un periodo específico.

                        **Características:**
                        - Filtra por rango de fechas (inicio y fin inclusive)
                        - Solo incluye órdenes confirmadas o entregadas
                        - Calcula cantidad inicial basándose en movimientos de inventario
                        - Resultados ordenados por cantidad vendida (descendente)
                        - Soporte para paginación

                        **Información retornada por producto:**
                        - Nombre del producto y categoría
                        - Precio de venta actual
                        - Cantidad inicial (stock al inicio del periodo)
                        - Cantidad vendida en el periodo
                        - Cantidad actual en stock

                        **Requiere autenticación y rol ADMIN.**
                        """, security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.utils.ApiResult.class), examples = @ExampleObject(name = "Ejemplo de reporte", value = """
                                        {
                                          "success": true,
                                          "message": "Reporte de productos más vendidos generado exitosamente",
                                          "data": [
                                            {
                                              "productName": "Laptop HP Pavilion",
                                              "categoryName": "Electrónica",
                                              "currentPrice": 45000.00,
                                              "initialQuantity": 100,
                                              "soldQuantity": 35,
                                              "currentQuantity": 65
                                            },
                                            {
                                              "productName": "Mouse Logitech",
                                              "categoryName": "Accesorios",
                                              "currentPrice": 1500.00,
                                              "initialQuantity": 200,
                                              "soldQuantity": 28,
                                              "currentQuantity": 172
                                            }
                                          ]
                                        }
                                                                    """))),
                        @ApiResponse(responseCode = "400", description = "Parámetros de fecha inválidos", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de validación", value = """
                                        {
                                          "statusCode": 400,
                                          "errorCode": "VALIDATION_ERROR",
                                          "message": "La fecha de fin no puede ser anterior a la fecha de inicio",
                                          "details": ["Rango de fechas inválido"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/most-sold"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autenticación", value = """
                                        {
                                          "statusCode": 401,
                                          "errorCode": "AUTH_ERROR",
                                          "message": "Acceso no autorizado. Token inválido o ausente",
                                          "details": ["Token inválido, ausente o expirado"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/most-sold"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo rol ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autorización", value = """
                                        {
                                          "statusCode": 403,
                                          "errorCode": "ACCESS_DENIED",
                                          "message": "Acceso denegado",
                                          "details": ["No tiene permisos suficientes para acceder a este recurso"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/most-sold"
                                        }
                                        """)))
        })
        public @interface GetMostSoldProductsDoc {
        }

        @Target({ ElementType.PARAMETER })
        @Retention(RetentionPolicy.RUNTIME)
        @Parameter(name = "startDate", description = "Fecha de inicio del periodo (formato: YYYY-MM-DD)", example = "2025-11-01", required = true)
        public @interface StartDateParam {
        }

        @Target({ ElementType.PARAMETER })
        @Retention(RetentionPolicy.RUNTIME)
        @Parameter(name = "endDate", description = "Fecha de fin del periodo (formato: YYYY-MM-DD)", example = "2025-12-07", required = true)
        public @interface EndDateParam {
        }

        @Target({ ElementType.METHOD })
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(summary = "Obtener reporte de costos mensuales", description = """
                        Genera un reporte de costos y ventas agrupado por mes para un año específico.

                        **Características:**
                        - Filtra por año (requerido)
                        - Permite filtrar por categoría (opcional)
                        - Permite filtrar por producto específico (opcional)
                        - Retorna datos de los 12 meses del año
                        - Solo incluye órdenes confirmadas o entregadas
                        - Calcula costos promedio de compra desde movimientos de inventario

                        **Información retornada por mes:**
                        - Nombre del mes
                        - Unidades vendidas
                        - Precio unitario promedio de venta
                        - Costo unitario promedio de compra
                        - Costo total promedio (cantidad × costo unitario)
                        - Variación porcentual de costos respecto al mes anterior

                        **Requiere autenticación y rol ADMIN.**
                        """, security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.utils.ApiResult.class), examples = @ExampleObject(name = "Ejemplo de reporte", value = """
                                        {
                                          "success": true,
                                          "message": "Reporte de costos mensuales generado exitosamente",
                                          "data": [
                                            {
                                              "month": "Enero",
                                              "unitsSold": 150,
                                              "averageSellingPrice": 25000.00,
                                              "averagePurchaseCost": 18000.00,
                                              "totalAverageCost": 2700000.00,
                                              "costVariationPercentage": 0.0
                                            },
                                            {
                                              "month": "Febrero",
                                              "unitsSold": 175,
                                              "averageSellingPrice": 26000.00,
                                              "averagePurchaseCost": 19000.00,
                                              "totalAverageCost": 3325000.00,
                                              "costVariationPercentage": 23.15
                                            }
                                          ]
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Año inválido o fuera de rango", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de validación", value = """
                                        {
                                          "statusCode": 400,
                                          "errorCode": "VALIDATION_ERROR",
                                          "message": "Año fuera de rango permitido",
                                          "details": ["El año debe estar entre 2000 y 2100"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/cost"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autenticación", value = """
                                        {
                                          "statusCode": 401,
                                          "errorCode": "AUTH_ERROR",
                                          "message": "Acceso no autorizado. Token inválido o ausente",
                                          "details": ["Token inválido, ausente o expirado"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/cost"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo rol ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autorización", value = """
                                        {
                                          "statusCode": 403,
                                          "errorCode": "ACCESS_DENIED",
                                          "message": "Acceso denegado",
                                          "details": ["No tiene permisos suficientes para acceder a este recurso"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/cost"
                                        }
                                        """)))
        })
        public @interface GetCostReportDoc {
        }

        @Target({ ElementType.PARAMETER })
        @Retention(RetentionPolicy.RUNTIME)
        @Parameter(name = "year", description = "Año para el reporte (formato: YYYY)", example = "2025", required = true)
        public @interface YearParam {
        }

        @Target({ ElementType.PARAMETER })
        @Retention(RetentionPolicy.RUNTIME)
        @Parameter(name = "categoryId", description = "ID de categoría para filtrar (opcional)", example = "123e4567-e89b-12d3-a456-426614174000")
        public @interface CategoryIdParam {
        }

        @Target({ ElementType.PARAMETER })
        @Retention(RetentionPolicy.RUNTIME)
        @Parameter(name = "categoryName", description = "Nombre de la categoría para filtrar (opcional). No distingue mayúsculas/minúsculas.", example = "Postres")
        public @interface CategoryNameParam {
        }

        @Target({ ElementType.PARAMETER })
        @Retention(RetentionPolicy.RUNTIME)
        @Parameter(name = "productName", description = """
                        Nombre del producto a consultar.

                        El nombre debe coincidir exactamente con el nombre del producto en el sistema (no distingue mayúsculas/minúsculas).
                        """, required = true, example = "Laptop HP", schema = @Schema(type = "string"))
        public @interface ProductNameParam {
        }

        @Target({ ElementType.METHOD })
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(summary = "Obtener reporte de stock diario", description = """
                        Genera un reporte de evolución del stock de un producto día a día en un período determinado.

                        **Características:**
                        - Requiere ID de producto específico
                        - Filtra por rango de fechas (inicio y fin inclusive)
                        - Retorna datos para TODOS los días del período
                        - Calcula stock inicial del período desde histórico de movimientos
                        - Incluye días sin movimientos

                        **Información retornada por día:**
                        - Fecha del día
                        - Stock al inicio del día
                        - Entradas (movimientos IN) del día
                        - Salidas (movimientos OUT) del día
                        - Stock al final del día (calculado)
                        - Variación porcentual de stock vs día anterior

                        **Requiere autenticación y rol ADMIN.**
                        """, security = @SecurityRequirement(name = "bearer-key"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.utils.ApiResult.class), examples = @ExampleObject(name = "Ejemplo de reporte", value = """
                                        {
                                          "success": true,
                                          "message": "Reporte de stock diario generado exitosamente",
                                          "data": [
                                            {
                                              "date": "2024-12-01",
                                              "startDayStock": 100,
                                              "entries": 20,
                                              "exits": 15,
                                              "endDayStock": 105,
                                              "variationPercentage": 5.0
                                            },
                                            {
                                              "date": "2024-12-02",
                                              "startDayStock": 105,
                                              "entries": 10,
                                              "exits": 8,
                                              "endDayStock": 107,
                                              "variationPercentage": 1.9
                                            }
                                          ]
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Fechas inválidas o producto no encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de validación", value = """
                                        {
                                          "statusCode": 400,
                                          "errorCode": "VALIDATION_ERROR",
                                          "message": "Producto no encontrado",
                                          "details": ["No existe un producto con el nombre especificado"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/stock"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autenticación", value = """
                                        {
                                          "statusCode": 401,
                                          "errorCode": "AUTH_ERROR",
                                          "message": "Acceso no autorizado. Token inválido o ausente",
                                          "details": ["Token inválido, ausente o expirado"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/stock"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo rol ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autorización", value = """
                                        {
                                          "statusCode": 403,
                                          "errorCode": "ACCESS_DENIED",
                                          "message": "Acceso denegado",
                                          "details": ["No tiene permisos suficientes para acceder a este recurso"],
                                          "timestamp": "2024-12-09T05:45:00.000000000Z",
                                          "path": "/api/reports/products/stock"
                                        }
                                        """)))
        })
        public @interface GetStockReportDoc {
        }
}
