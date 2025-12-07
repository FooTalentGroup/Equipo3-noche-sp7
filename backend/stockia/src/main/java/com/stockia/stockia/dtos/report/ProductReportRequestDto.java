package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO para los parámetros de búsqueda de reportes de productos.
 * Permite filtrar por rango de fechas.
 *
 * @param startDate Fecha de inicio del periodo (inclusive)
 * @param endDate   Fecha de fin del periodo (inclusive)
 */
@Schema(description = "Parámetros de búsqueda para reportes de productos")
public record ProductReportRequestDto(

        @NotNull(message = "La fecha de inicio es obligatoria") @Schema(description = "Fecha de inicio del periodo (inclusive)", example = "2025-11-01", required = true) LocalDate startDate,

        @NotNull(message = "La fecha de fin es obligatoria") @Schema(description = "Fecha de fin del periodo (inclusive)", example = "2025-12-07", required = true) LocalDate endDate) {
    /**
     * Validación personalizada para asegurar que la fecha de fin sea mayor o igual
     * a la fecha de inicio.
     */
    public ProductReportRequestDto {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser mayor o igual a la fecha de inicio");
        }
    }
}
