package com.stockia.stockia.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un producto (todos opcionales)")
public class ProductUpdateDto {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nuevo nombre del producto (único)", example = "Laptop HP Pavilion 15 (Actualizado)")
    private String name;

    @Schema(description = "Nuevo ID de categoría", example = "2")
    private UUID categoryId;

    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    @Schema(description = "Nuevo precio de venta", example = "899.99")
    private BigDecimal price;

    @Pattern(regexp = "^(https?://.*)?$", message = "La URL de la foto debe ser válida")
    @Schema(description = "Nueva URL de la imagen", example = "https://example.com/images/laptop-new.jpg")
    private String photoUrl;

    @Min(value = 0, message = "El stock actual debe ser mayor o igual a 0")
    @Schema(description = "Nueva cantidad en stock (editable manualmente)", example = "15")
    private Integer currentStock;

    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    @Schema(description = "Nuevo stock mínimo para alertas", example = "8")
    private Integer minStock;

    @Schema(description = "Nueva disponibilidad del producto", example = "true")
    private Boolean isAvailable;
}
