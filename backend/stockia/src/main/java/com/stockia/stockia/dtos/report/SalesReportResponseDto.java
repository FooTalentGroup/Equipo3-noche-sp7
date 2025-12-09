package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de respuesta completa para el reporte de ventas.
 * Contiene todas las métricas y datos necesarios para visualizar el reporte.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta completa del reporte de ventas")
public class SalesReportResponseDto {

    @Schema(description = "Métricas principales con comparación de períodos")
    private SalesMetricsDto metrics;

    @Schema(description = "Datos de ventas diarias para gráfico de barras")
    private List<DailySalesDto> dailySales;

    @Schema(description = "Distribución de ventas por método de pago para gráfico de dona")
    private List<PaymentMethodDistributionDto> paymentMethodDistribution;

    @Schema(description = "Fecha de inicio del período consultado", example = "2025-11-01")
    private LocalDate startDate;

    @Schema(description = "Fecha de fin del período consultado", example = "2025-11-30")
    private LocalDate endDate;

    @Schema(description = "Nombre del producto filtrado (opcional)", example = "Laptop HP")
    private String productName;
}
