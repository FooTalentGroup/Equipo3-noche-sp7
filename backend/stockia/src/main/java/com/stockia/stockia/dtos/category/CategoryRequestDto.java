package com.stockia.stockia.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar una categoría de producto.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    /**
     * Nombre de la categoría.
     * Requerido, máximo 50 caracteres.
     */
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String name;

    /**
     * Descripción de la categoría.
     * Opcional, máximo 255 caracteres.
     */
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;

    /**
     * Estado de la categoría.
     * Por defecto es true (activa).
     */
    @Builder.Default
    private Boolean isActive = true;
}


