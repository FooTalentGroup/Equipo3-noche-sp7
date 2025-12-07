package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para la respuesta del reporte de productos más vendidos.
 *
 * @param productId       ID del producto
 * @param productName     Nombre del producto
 * @param categoryName    Nombre de la categoría del producto
 * @param salePrice       Precio de venta actual del producto
 * @param initialQuantity Cantidad de stock al inicio del periodo
 * @param quantitySold    Cantidad total vendida en el periodo
 * @param currentQuantity Cantidad actual en stock
 */
@Schema(description = "Datos del reporte de productos más vendidos")
public record MostSoldProductDto(

                @Schema(description = "ID del producto", example = "123e4567-e89b-12d3-a456-426614174000") UUID productId,

                @Schema(description = "Nombre del producto", example = "Laptop HP Pavilion") String productName,

                @Schema(description = "Nombre de la categoría", example = "Electrónica") String categoryName,

                @Schema(description = "Precio de venta actual", example = "1250.00") BigDecimal salePrice,

                @Schema(description = "Cantidad de stock al inicio del periodo", example = "50") Long initialQuantity,

                @Schema(description = "Cantidad total vendida en el periodo", example = "25") Long quantitySold,

                @Schema(description = "Cantidad actual en stock", example = "25") Integer currentQuantity) {
}
