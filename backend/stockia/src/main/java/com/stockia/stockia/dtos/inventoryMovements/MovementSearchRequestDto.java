package com.stockia.stockia.dtos.inventoryMovements;

import com.stockia.stockia.enums.MovementType;

import java.util.UUID;

public record MovementSearchRequestDto(
        UUID productId,
        MovementType movementType,
        UUID userId
) {
}
