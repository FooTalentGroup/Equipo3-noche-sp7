package com.stockia.stockia.dtos.category;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para filtrar y buscar categorías de productos.
 *
 * @param name Filtro por nombre (búsqueda parcial, case-insensitive)
 * @param isActive Filtro por estado activo/inactivo
 * @param deleted Filtro por estado de eliminación (soft delete)
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Schema(description = "Parámetros de búsqueda y filtrado para categorías")
public record CategorySearchRequestDto(

        @Parameter(description = "Filtro por nombre de categoría (búsqueda parcial)", example = "Electrónica")
        @Schema(description = "Nombre de la categoría (búsqueda parcial, case-insensitive)", example = "Electrónica")
        String name,

        @Parameter(description = "Filtro por estado activo (true = activas, false = inactivas, null = todas)", example = "true")
        @Schema(description = "Estado de activación de la categoría", example = "true")
        Boolean isActive,

        @Parameter(description = "Filtro por estado de eliminación (true = eliminadas, false = no eliminadas, null = todas)", example = "false")
        @Schema(description = "Estado de eliminación (soft delete)", example = "false")
        Boolean deleted
) {
}

