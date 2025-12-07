package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para parámetros del reporte de stock de productos.
 *
 * @param productId ID del producto a consultar (requerido)
 * @param startDate Fecha de inicio del período (requerido)
 * @param endDate   Fecha de fin del período (requerido)
 */
@Schema(description = "Parámetros para el reporte de stock de productos")
public record StockReportRequestDto(

        @Schema(description = "ID del producto", example = "123e4567-e89b-12d3-a456-426614174000", required = true) @NotNull(message = "El ID del producto es obligatorio") UUID productId,

        @Schema(description = "Fecha de inicio del período", example = "2025-11-12", required = true) @NotNull(message = "La fecha de inicio es obligatoria") LocalDate startDate,

        @Schema(description = "Fecha de fin del período", example = "2025-11-22", required = true) @NotNull(message = "La fecha de fin es obligatoria") LocalDate endDate) {
}
