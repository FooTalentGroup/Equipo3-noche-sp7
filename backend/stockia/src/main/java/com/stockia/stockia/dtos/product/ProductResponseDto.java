package com.stockia.stockia.dtos.product;

import com.stockia.stockia.dtos.category.CategoryResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un producto")
public class ProductResponseDto {

    @Schema(description = "Identificador único del producto", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nombre del producto", example = "Laptop HP Pavilion 15")
    private String name;

    @Schema(description = "Categoría a la que pertenece el producto")
    private CategoryResponseDto category;

    @Schema(description = "Precio de venta", example = "850.00")
    private BigDecimal price;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/laptop.jpg")
    private String photoUrl;

    @Schema(description = "Cantidad actual en stock", example = "10")
    private Integer currentStock;

    @Schema(description = "Stock mínimo para alertas", example = "5")
    private Integer minStock;

    @Schema(description = "Disponibilidad del producto", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Fecha y hora de creación", example = "2025-11-20T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2025-11-20T15:45:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Indica si el stock está por debajo del mínimo", example = "false")
    private Boolean hasLowStock;
}
