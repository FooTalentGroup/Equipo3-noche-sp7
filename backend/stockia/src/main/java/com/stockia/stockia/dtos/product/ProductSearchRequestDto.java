package com.stockia.stockia.dtos.product;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * DTO para filtrar y buscar productos.
 *
 * @param deleted Filtro por estado de eliminación (soft delete)
 * @param includeInactive Incluir productos inactivos (isAvailable=false)
 * @param lowStock Filtrar solo productos con stock bajo
 * @param q Búsqueda por nombre del producto (case-insensitive)
 * @param categoryId Filtrar por categoría específica
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Schema(description = "Parámetros de búsqueda y filtrado para productos")
public record ProductSearchRequestDto(

        @Parameter(description = "Filtro por estado de eliminación (true = eliminados, false = activos, null = todos)", example = "false")
        @Schema(description = "Estado de eliminación (soft delete)", example = "false", defaultValue = "false")
        Boolean deleted,

        @Parameter(description = "Incluir productos inactivos en los resultados", example = "false")
        @Schema(description = "Incluir productos inactivos (isAvailable=false)", example = "false", defaultValue = "false")
        Boolean includeInactive,

        @Parameter(description = "Filtrar solo productos con stock bajo (currentStock <= minStock)", example = "false")
        @Schema(description = "Solo productos con stock bajo", example = "false", defaultValue = "false")
        Boolean lowStock,

        @Parameter(description = "Búsqueda por nombre del producto (búsqueda parcial, case-insensitive)", example = "laptop")
        @Schema(description = "Nombre del producto (búsqueda parcial)", example = "laptop")
        String q,

        @Parameter(description = "Filtrar por ID de categoría", example = "123e4567-e89b-12d3-a456-426614174000")
        @Schema(description = "ID de la categoría", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID categoryId
) {
    /**
     * Constructor con valores por defecto.
     */
    public ProductSearchRequestDto {
        // Aplicar valores por defecto si son null
        deleted = deleted != null ? deleted : false;
        includeInactive = includeInactive != null ? includeInactive : false;
        lowStock = lowStock != null ? lowStock : false;
    }
}

