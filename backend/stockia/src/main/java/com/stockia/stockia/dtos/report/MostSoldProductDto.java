package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Datos del reporte de productos más vendidos")
public record MostSoldProductDto(

        @Schema(description = "ID del producto", example = "b86bc946-8b83-4b5c-a9b6-e0d66198f5bd") UUID productId,

        @Schema(description = "Nombre del producto", example = "helado de vainilla") String productName,

        @Schema(description = "Nombre de la categoría", example = "Postres") String categoryName,

        @Schema(description = "Precio de venta actual", example = "1250.00") BigDecimal salePrice,

        @Schema(description = "Cantidad de stock al inicio del periodo", example = "50") Long initialQuantity,

        @Schema(description = "Cantidad total vendida en el periodo", example = "25") Long quantitySold,

        @Schema(description = "Cantidad actual en stock", example = "25") Integer currentQuantity) {
}
