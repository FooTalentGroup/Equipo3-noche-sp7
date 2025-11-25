package com.stockia.stockia.dtos.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para solicitar la creación de un item dentro de una orden.
 * Contiene la información mínima necesaria para agregar un producto a la venta.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de un producto a incluir en la orden")
public class OrderItemRequestDto {

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "ID del producto a vender", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    private UUID productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Schema(description = "Cantidad de unidades a vender", example = "2", minimum = "1", required = true)
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio unitario debe ser mayor o igual a 0")
    @Schema(description = "Precio unitario al momento de la venta", example = "150.50", required = true)
    private BigDecimal unitPrice;
}
