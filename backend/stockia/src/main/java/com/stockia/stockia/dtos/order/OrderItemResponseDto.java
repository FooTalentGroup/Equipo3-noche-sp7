package com.stockia.stockia.dtos.order;

import com.stockia.stockia.dtos.product.ProductResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO de respuesta para items de orden.
 * Incluye información completa del producto vendido junto con cantidad y
 * totales.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información detallada de un item/producto dentro de una orden")
public class OrderItemResponseDto {

    @Schema(description = "ID único del item", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Información completa del producto")
    private ProductResponseDto product;

    @Schema(description = "Cantidad de unidades vendidas", example = "2")
    private Integer quantity;

    @Schema(description = "Precio unitario al momento de la venta", example = "150.50")
    private BigDecimal unitPrice;

    @Schema(description = "Total del item (cantidad × precio unitario)", example = "301.00")
    private BigDecimal itemTotal;
}
