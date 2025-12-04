package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementRequestDto;
import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementResponseDto;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.models.InventoryMovement;
import com.stockia.stockia.models.Product;
import com.stockia.stockia.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface InventoryMovementMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "movementType", source = "requestDto.movementType")
    @Mapping(target = "quantity", source = "requestDto.quantity")
    @Mapping(target = "reason", source = "requestDto.reason")
    @Mapping(target = "user", source = "loggedInUser")
    @Mapping(target = "newStock", source = "newStock")
    @Mapping(target = "purchaseCost", source = "requestDto.purchaseCost")
    @Mapping(target = "createdAt", ignore = true)
    InventoryMovement toEntity(InventoryMovementRequestDto requestDto, Product product, User loggedInUser, int newStock);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "movementType", source = "movementType")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "newStock", source = "newStock")
    @Mapping(target = "purchaseCost", source = "purchaseCost")
    @Mapping(target = "createdAt", source = "createdAt")
    InventoryMovementResponseDto toResponseDto(InventoryMovement inventoryMovement);

    default Page<InventoryMovementResponseDto> toResponseDto(Page<InventoryMovement> inventoryMovement) {
        return inventoryMovement.map(this::toResponseDto);
    }
}
