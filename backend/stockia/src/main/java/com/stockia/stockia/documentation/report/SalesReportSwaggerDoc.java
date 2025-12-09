package com.stockia.stockia.documentation.report;

import com.stockia.stockia.dtos.report.SalesReportResponseDto;
import com.stockia.stockia.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "10 - Reportes de Ventas", description = "Endpoints para reportes y análisis de ventas - Solo ADMIN")
public interface SalesReportSwaggerDoc {

  @Operation(summary = "Generar reporte de ventas", description = """
      Genera un reporte completo de ventas para el período especificado.

      **Requiere autenticación y rol ADMIN.**

      **Características:**
      - Métricas principales: ingreso total, cantidad de ventas, ticket promedio
      - Comparación con período anterior (% de cambio)
      - Ventas diarias para gráfico de barras
      - Distribución por método de pago para gráfico de dona
      - Filtro opcional por nombre de producto

      **Criterios:**
      - Solo considera órdenes con estado CONFIRMED o DELIVERED
      - El período anterior se calcula automáticamente con la misma duración
      - Si no hay ventas en el período, retorna valores en cero

      **Ejemplos de uso:**
      - Reporte de ventas del mes actual
      - Análisis de ventas por producto específico
      - Comparación de períodos personalizados
      """, security = @SecurityRequirement(name = "bearer-key"))
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class), examples = @ExampleObject(name = "Reporte exitoso", value = """
          {
            "success": true,
            "message": "Reporte generado exitosamente",
            "data": {
              "metrics": {
                "totalRevenue": 2500000.00,
                "totalOrders": 150,
                "averageTicket": 45000.00,
                "revenueChangePercentage": 12.5,
                "ordersChangePercentage": 8.3,
                "averageTicketChangePercentage": 3.8
              },
              "dailySales": [
                {
                  "date": "2025-12-01",
                  "totalAmount": 125000.00
                }
              ],
              "paymentMethodDistribution": [
                {
                  "paymentMethod": "CREDIT_CARD",
                  "totalAmount": 1500000.00,
                  "percentage": 60.0
                },
                {
                  "paymentMethod": "CASH",
                  "totalAmount": 1000000.00,
                  "percentage": 40.0
                }
              ],
              "startDate": "2025-12-01",
              "endDate": "2025-12-31",
              "productName": null
            }
          }
          """))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros inválidos - fecha de fin anterior a fecha de inicio", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class), examples = @ExampleObject(name = "Error de validación", value = """
          {
            "success": false,
            "message": "La fecha de fin no puede ser anterior a la fecha de inicio",
            "data": null
          }
          """))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado - token JWT inválido o ausente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autenticación", value = """
          {
            "statusCode": 401,
            "errorCode": "AUTH_ERROR",
            "message": "Acceso no autorizado. Token inválido o ausente",
            "details": [
              "Token inválido, ausente o expirado",
              "Se requiere autenticación para acceder a este recurso"
            ],
            "timestamp": "2024-12-09T05:32:12.427569800Z",
            "path": "/api/reports/sales"
          }
          """))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado - solo usuarios ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error de autorización", value = """
          {
            "statusCode": 403,
            "errorCode": "ACCESS_DENIED",
            "message": "Acceso denegado",
            "details": [
              "No tiene permisos suficientes para acceder a este recurso"
            ],
            "timestamp": "2024-12-09T05:45:00.000000000Z",
            "path": "/api/reports/sales"
          }
          """))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.stockia.stockia.exceptions.ErrorResponse.class), examples = @ExampleObject(name = "Error interno", value = """
          {
            "statusCode": 500,
            "errorCode": "INTERNAL_SERVER_ERROR",
            "message": "Ha ocurrido un error inesperado en el servidor",
            "details": [
              "Error al procesar la solicitud",
              "Por favor, contacte al administrador del sistema"
            ],
            "timestamp": "2024-12-09T05:32:12.427569800Z",
            "path": "/api/reports/sales"
          }
          """)))
  })
  ResponseEntity<ApiResult<SalesReportResponseDto>> generateSalesReport(
      @Parameter(description = "Fecha de inicio del período de reporte", required = true, example = "2025-12-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

      @Parameter(description = "Fecha de fin del período de reporte", required = true, example = "2025-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

      @Parameter(description = "Nombre del producto para filtrar (opcional)", required = false, example = "Porción de Cheesecake") @RequestParam(required = false) String productName);
}
