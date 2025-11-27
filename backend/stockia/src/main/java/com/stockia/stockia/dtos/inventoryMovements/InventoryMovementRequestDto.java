package com.stockia.stockia.dtos.inventoryMovements;

import com.stockia.stockia.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryMovementRequestDto (
        @Schema(description = "ID del producto")
        @NotNull(message = "El productId es obligatorio")
        UUID productId,
        @Schema(description = "Tipo de movimiento: IN, OUT o ADJUSTMENT")
        @NotNull(message = "El movementType es obligatorio")
        MovementType movementType,
        @Schema(description = "Cantidad del movimiento")
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser un número positivo")
        Integer quantity,
        @Schema(description = "Razón o comentario del movimiento", example = "Ajuste por rotura")
        @Size(max = 255, message = "La razón no puede superar los 255 caracteres")
        String reason,
        @Schema(description = "Costo de compra (solo para movimientos IN)")
        @Positive(message = "El purchaseCost debe ser positivo")
        BigDecimal purchaseCost
){
}
