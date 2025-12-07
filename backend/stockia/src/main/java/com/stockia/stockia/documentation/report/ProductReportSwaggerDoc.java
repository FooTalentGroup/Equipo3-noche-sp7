package com.stockia.stockia.documentation.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
        @Tag(name = "Reportes de Productos", description = "Endpoints para generar reportes y estadísticas de productos")
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
                        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.utils.ApiResult.class))),
                        @ApiResponse(responseCode = "400", description = "Parámetros de fecha inválidos (fecha fin menor que fecha inicio)", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo rol ADMIN puede acceder", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
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
                        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.utils.ApiResult.class))),
                        @ApiResponse(responseCode = "400", description = "Año inválido o fuera de rango", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo rol ADMIN puede acceder", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
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
        @Parameter(name = "productId", description = "ID de producto para filtrar (opcional)", example = "123e4567-e89b-12d3-a456-426614174000")
        public @interface ProductIdParam {
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
                        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.stockia.stockia.utils.ApiResult.class))),
                        @ApiResponse(responseCode = "400", description = "Fechas inválidas o producto no encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo rol ADMIN puede acceder", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        })
        public @interface GetStockReportDoc {
        }
}
