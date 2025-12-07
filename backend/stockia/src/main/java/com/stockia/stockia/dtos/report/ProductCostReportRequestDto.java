package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para parámetros del reporte de costos de productos.
 *
 * @param year       Año a consultar (requerido)
 * @param categoryId ID de categoría para filtrar (opcional)
 * @param productId  ID de producto específico para filtrar (opcional)
 */
@Schema(description = "Parámetros para el reporte de costos de productos")
public record ProductCostReportRequestDto(

        @Schema(description = "Año para el reporte", example = "2025", required = true) @NotNull(message = "El año es obligatorio") @Min(value = 2000, message = "El año debe ser mayor o igual a 2000") @Max(value = 2100, message = "El año debe ser menor o igual a 2100") Integer year,

        @Schema(description = "ID de categoría para filtrar (opcional)", example = "123e4567-e89b-12d3-a456-426614174000") UUID categoryId,

        @Schema(description = "ID de producto para filtrar (opcional)", example = "123e4567-e89b-12d3-a456-426614174000") UUID productId) {
}
