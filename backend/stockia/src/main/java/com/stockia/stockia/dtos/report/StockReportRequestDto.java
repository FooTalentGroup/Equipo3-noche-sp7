package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Parámetros para el reporte de stock de productos")
public record StockReportRequestDto(

                @Schema(description = "Nombre del producto", example = "Laptop HP", required = true) @NotBlank(message = "El nombre del producto es obligatorio") String productName,

                @Schema(description = "Fecha de inicio del período", example = "2025-11-12", required = true) @NotNull(message = "La fecha de inicio es obligatoria") LocalDate startDate,

                @Schema(description = "Fecha de fin del período", example = "2025-11-22", required = true) @NotNull(message = "La fecha de fin es obligatoria") LocalDate endDate) {
}
