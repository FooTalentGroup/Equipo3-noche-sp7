package com.stockia.stockia.dtos.inventoryMovements;

import com.stockia.stockia.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryMovementResponseDto (
        UUID id,
        UUID productId,
        String productName,
        MovementType movementType,
        Integer quantity,
        String reason,
        UUID userId,
        String userName,
        Integer newStock,
        BigDecimal purchaseCost,
        LocalDateTime createdAt
){
}
