package com.stockia.stockia.dtos.inventoryMovements;

import com.stockia.stockia.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Parámetros de búsqueda y filtrado para movimientos de stock")
public record MovementSearchRequestDto(
        @Schema(description = "ID del producto",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        UUID productId,

        @Schema(description = "Nombre del producto",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String productName,

        @Schema(description = "Tipo de movimiento (IN, OUT, ADJUSTMENT)",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        MovementType movementType,

        @Schema(description = "ID del usuario",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        UUID userId,

        @Schema(description = "Fecha de inicio del rango de búsqueda (formato: YYYY-MM-DD)",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDate startDate,

        @Schema(description = "Fecha de fin del rango de búsqueda (formato: YYYY-MM-DD)",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDate endDate
) {
}
