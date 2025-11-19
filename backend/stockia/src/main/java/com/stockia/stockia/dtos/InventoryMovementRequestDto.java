package com.stockia.stockia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Ests DTO recibe los datos desde frontend
 * No incluye createdAt porque se genera automaticamente
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryMovementRequestDto {
    private Long id;
    private Long userId;
    private String movementType;
    private int quantity;
    private String reason;


    
}
