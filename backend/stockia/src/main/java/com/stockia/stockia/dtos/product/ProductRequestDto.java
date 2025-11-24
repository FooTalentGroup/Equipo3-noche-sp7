package com.stockia.stockia.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
@Schema(description = "Datos necesarios para registrar un nuevo producto")
public class ProductRequestDto {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre del producto (único en el sistema)", example = "Laptop HP Pavilion 15")
    private String name;

    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "ID de la categoría del producto", example = "1")
    private UUID categoryId;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    @Schema(description = "Precio de venta del producto", example = "850.00")
    private BigDecimal price;

    @Pattern(regexp = "^(https?://.*)?$", message = "La URL de la foto debe ser válida")
    @Schema(description = "URL de la imagen del producto (opcional)", example = "https://example.com/images/laptop.jpg")
    private String photoUrl;

    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    @Schema(description = "Stock mínimo para alertas (opcional, por defecto 5)", example = "5")
    private Integer minStock;
}
