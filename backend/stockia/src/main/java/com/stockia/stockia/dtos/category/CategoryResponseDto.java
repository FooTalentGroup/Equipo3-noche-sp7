package com.stockia.stockia.dtos.category;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para una categoría de producto.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    /**
     * ID único de la categoría.
     */
    private UUID id;

    /**
     * Nombre de la categoría.
     */
    private String name;

    /**
     * Descripción de la categoría.
     */
    private String description;

    /**
     * Indica si la categoría está activa.
     */
    private Boolean isActive;

    /**
     * Cantidad de productos asociados a esta categoría.
     */
    private Integer productCount;
}
