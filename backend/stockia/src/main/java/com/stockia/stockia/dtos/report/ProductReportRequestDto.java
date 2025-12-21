package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Parámetros de búsqueda para reportes de productos")
public record ProductReportRequestDto(

        @NotNull(message = "La fecha de inicio es obligatoria") @Schema(description = "Fecha de inicio del periodo (inclusive)", example = "2025-11-01", required = true) LocalDate startDate,

        @NotNull(message = "La fecha de fin es obligatoria") @Schema(description = "Fecha de fin del periodo (inclusive)", example = "2025-12-07", required = true) LocalDate endDate) {

    public ProductReportRequestDto {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser mayor o igual a la fecha de inicio");
        }
    }
}
