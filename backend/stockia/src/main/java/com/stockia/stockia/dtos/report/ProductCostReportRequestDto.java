package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para parámetros del reporte de costos de productos.
 *
 * @param year         Año a consultar (requerido)
 * @param categoryName Nombre de la categoría para filtrar (opcional)
 * @param productName  Nombre del producto para filtrar (opcional)
 */
@Schema(description = "Parámetros para el reporte de costos de productos")
public record ProductCostReportRequestDto(

                @Schema(description = "Año para el reporte", example = "2025", required = true) @NotNull(message = "El año es obligatorio") @Min(value = 2000, message = "El año debe ser mayor o igual a 2000") @Max(value = 2100, message = "El año debe ser menor o igual a 2100") Integer year,

                @Schema(description = "Nombre de la categoría para filtrar (opcional)", example = "Postres") String categoryName,

                @Schema(description = "Nombre del producto para filtrar (opcional)", example = "helado de vainilla") String productName) {
}
