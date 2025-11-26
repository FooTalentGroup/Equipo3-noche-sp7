package com.stockia.stockia.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar una categoría de producto.
 * Todos los campos son opcionales - solo se actualizan los campos proporcionados.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar una categoría. Todos los campos son opcionales - solo se actualizan los campos proporcionados.")
public class CategoryUpdateDto {

    /**
     * Nuevo nombre de la categoría.
     * Opcional, máximo 50 caracteres.
     */
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Schema(description = "Nuevo nombre de la categoría (único)", example = "Electrónica y Tecnología", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * Nueva descripción de la categoría.
     * Opcional, máximo 255 caracteres.
     */
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Schema(description = "Nueva descripción de la categoría", example = "Dispositivos electrónicos, computadoras y accesorios", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    /**
     * Nuevo estado de la categoría.
     * Opcional.
     */
    @Schema(description = "Nuevo estado de activación de la categoría", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isActive;
}

