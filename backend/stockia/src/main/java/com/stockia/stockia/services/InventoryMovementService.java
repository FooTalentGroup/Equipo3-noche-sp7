package com.stockia.stockia.services;

import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementRequestDto;
import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementResponseDto;
import com.stockia.stockia.dtos.inventoryMovements.MovementSearchRequestDto;
import com.stockia.stockia.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InventoryMovementService {
    Page<InventoryMovementResponseDto> searchInventoryMovements(MovementSearchRequestDto params, Pageable pageable);
    InventoryMovementResponseDto findById(UUID id);

    InventoryMovementResponseDto registerInventoryMovement(InventoryMovementRequestDto dto, CustomUserDetails userDetails);
}
