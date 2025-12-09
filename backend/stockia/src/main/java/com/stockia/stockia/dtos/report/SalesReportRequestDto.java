package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para solicitar el reporte de ventas.
 * Permite filtrar por producto y período de tiempo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parámetros para generar el reporte de ventas")
public class SalesReportRequestDto {

    @Schema(description = "Nombre del producto a filtrar (opcional)", example = "Laptop HP")
    private String productName;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio del período", example = "2025-11-01", required = true)
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Schema(description = "Fecha de fin del período", example = "2025-11-30", required = true)
    private LocalDate endDate;

    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !endDate.isBefore(startDate);
    }
}
