package com.stockia.stockia.dtos;

import com.stockia.stockia.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para registrar un movimiento de inventario.
 * Recibe los datos desde el frontend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryMovementRequestDto {
    private UUID productId;
    private MovementType movementType;
    private Integer quantity;
    private String reason;
    private BigDecimal purchaseCost; // Solo para movimientos de tipo IN
}
