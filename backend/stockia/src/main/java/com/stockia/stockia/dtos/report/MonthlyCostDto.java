package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO para datos mensuales del reporte de costos.
 *
 * @param month                Número del mes (1-12)
 * @param monthName            Nombre del mes en español
 * @param unitsSold            Total de unidades vendidas en el mes
 * @param avgUnitPrice         Precio unitario promedio de venta
 * @param avgUnitCost          Costo unitario promedio de compra
 * @param totalAvgCost         Costo total promedio (unitsSold × avgUnit cost)
 * @param costVariationPercent Variación porcentual de costos vs mes anterior
 */
@Schema(description = "Datos mensuales del reporte de costos")
public record MonthlyCostDto(

        @Schema(description = "Número del mes", example = "1") Integer month,

        @Schema(description = "Nombre del mes", example = "Enero") String monthName,

        @Schema(description = "Unidades vendidas en el mes", example = "150") Long unitsSold,

        @Schema(description = "Precio unitario promedio de venta", example = "150.00") BigDecimal avgUnitPrice,

        @Schema(description = "Costo unitario promedio de compra", example = "42.00") BigDecimal avgUnitCost,

        @Schema(description = "Costo total promedio", example = "6300.00") BigDecimal totalAvgCost,

        @Schema(description = "Variación porcentual de costos respecto al mes anterior", example = "+15.9") BigDecimal costVariationPercent) {

    public MonthlyCostDto(Integer month, Long unitsSold, Double avgUnitPrice) {
        this(month, getMonthName(month), unitsSold,
                avgUnitPrice != null ? BigDecimal.valueOf(avgUnitPrice) : BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    private static String getMonthName(Integer month) {
        return switch (month) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            case 12 -> "Diciembre";
            default -> "Desconocido";
        };
    }

    public MonthlyCostDto withCostData(BigDecimal avgUnitCost, BigDecimal totalAvgCost,
            BigDecimal costVariationPercent) {
        return new MonthlyCostDto(month, monthName, unitsSold, avgUnitPrice,
                avgUnitCost, totalAvgCost, costVariationPercent);
    }

    public static MonthlyCostDto empty(Integer month) {
        return new MonthlyCostDto(month, getMonthName(month), 0L,
                BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
