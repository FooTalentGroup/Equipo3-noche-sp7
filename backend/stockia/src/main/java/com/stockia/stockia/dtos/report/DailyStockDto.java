package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para datos diarios del reporte de stock.
 *
 * @param date                  Fecha del día
 * @param initialStock          Stock al inicio del día
 * @param entries               Total de entradas (IN) del día
 * @param exits                 Total de salidas (OUT) del día
 * @param currentStock          Stock al final del día
 * @param stockVariationPercent Variación porcentual vs día anterior
 */
@Schema(description = "Datos diarios del reporte de stock")
public record DailyStockDto(

        @Schema(description = "Fecha del día", example = "2025-11-12") LocalDate date,

        @Schema(description = "Stock al inicio del día", example = "69") Integer initialStock,

        @Schema(description = "Total de entradas del día", example = "0") Integer entries,

        @Schema(description = "Total de salidas del día", example = "10") Integer exits,

        @Schema(description = "Stock al final del día", example = "59") Integer currentStock,

        @Schema(description = "Variación porcentual de stock vs día anterior", example = "-14.5") BigDecimal stockVariationPercent) {

    /**
     * DTO con datos calculados y variación porcentual
     */
    public static DailyStockDto create(LocalDate date, Integer initialStock,
            Integer entries, Integer exits,
            BigDecimal variation) {
        Integer currentStock = initialStock + entries - exits;
        return new DailyStockDto(date, initialStock, entries, exits,
                currentStock, variation);
    }

    /**
     * DTO para un día sin movimientos
     */
    public static DailyStockDto withNoMovements(LocalDate date, Integer stock) {
        return new DailyStockDto(date, stock, 0, 0, stock, BigDecimal.ZERO);
    }
}
