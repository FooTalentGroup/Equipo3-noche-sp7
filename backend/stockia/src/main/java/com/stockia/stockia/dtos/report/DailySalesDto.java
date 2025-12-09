package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para representar las ventas diarias.
 * Usado para generar el gráfico de barras del reporte de ventas.
 */
@Data
@Builder
@NoArgsConstructor
@Schema(description = "Datos de ventas diarias para gráfico de barras")
public class DailySalesDto {

    @Schema(description = "Fecha del registro de ventas", example = "2025-11-15")
    private LocalDate date;
    @Schema(description = "Monto total de ventas del día", example = "1400000.00")
    private BigDecimal totalAmount;

    public DailySalesDto(LocalDate date, BigDecimal totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }
}
