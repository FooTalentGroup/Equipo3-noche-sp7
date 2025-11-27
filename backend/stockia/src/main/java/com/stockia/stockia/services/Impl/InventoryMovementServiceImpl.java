package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementResponseDto;
import com.stockia.stockia.dtos.inventoryMovements.MovementSearchRequestDto;
import com.stockia.stockia.exceptions.ResourceNotFoundException;
import com.stockia.stockia.mappers.InventoryMovementMapper;
import com.stockia.stockia.services.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementRequestDto;
import com.stockia.stockia.enums.MovementType;
import com.stockia.stockia.models.InventoryMovement;
import com.stockia.stockia.models.Product;
import com.stockia.stockia.models.User;
import com.stockia.stockia.repositories.InventoryMovementRepository;
import com.stockia.stockia.repositories.ProductRepository;
import com.stockia.stockia.repositories.UserRepository;
import com.stockia.stockia.security.CustomUserDetails;
import com.stockia.stockia.exceptions.UserNotFoundException;
import com.stockia.stockia.exceptions.product.ProductNotFoundException;
import com.stockia.stockia.exceptions.product.InsufficientStockException;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InventoryMovementServiceImpl implements InventoryMovementService {
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final InventoryMovementMapper inventoryMovementMapper;

    @Override
    public Page<InventoryMovementResponseDto> searchInventoryMovements(MovementSearchRequestDto params,
                                                                    Pageable pageable) {
        Page<InventoryMovement> inventoryMovements = inventoryMovementRepository.searchInventoryMovements(
                params.productId(),
                params.movementType(),
                params.userId(),
                pageable);
        return inventoryMovementMapper.toResponseDto(inventoryMovements);
    }

    @Override
    public InventoryMovementResponseDto findById(UUID id) {
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento de inventario no encontrado con ID: " + id));
        return inventoryMovementMapper.toResponseDto(inventoryMovement);
    }

    @Transactional
    @Override
    public InventoryMovementResponseDto registerInventoryMovement(InventoryMovementRequestDto requestDto, CustomUserDetails userDetails) {
        Product product = productRepository.findById(requestDto.productId())
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + requestDto.productId()));

        User loggedInUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userDetails.getId()));

        if (requestDto.movementType() == MovementType.IN && requestDto.purchaseCost() == null) {
            throw new IllegalArgumentException("El purchaseCost es obligatorio para movimientos IN");
        }
        if (requestDto.movementType() != MovementType.IN && requestDto.purchaseCost() != null) {
            throw new IllegalArgumentException("El purchaseCost solo debe enviarse movimientos de tipo IN");
        }
        /*int newStock = product.getCurrentStock();
        // Calcular el nuevo stock según el tipo de movimiento
        if (requestDto.movementType() == MovementType.IN) {
            newStock += requestDto.quantity();
        } else if (requestDto.movementType() == MovementType.OUT) {
            newStock -= requestDto.quantity();
        } else if (requestDto.movementType() == MovementType.ADJUSTMENT) {
            newStock = requestDto.quantity();// Para ajustes, quantity es el nuevo valor absoluto
        }*/
        int newStock = switch (requestDto.movementType()) {
            case IN -> product.getCurrentStock() + requestDto.quantity();
            case OUT -> product.getCurrentStock() - requestDto.quantity();
            case ADJUSTMENT -> requestDto.quantity();
        };



        if (newStock < 0) {
            throw new InsufficientStockException(product.getCurrentStock(), Math.abs(requestDto.quantity()));
        }

        // Actualizar el stock del producto
        product.setCurrentStock(newStock);
        productRepository.save(product);

        InventoryMovement inventoryMovement = inventoryMovementMapper.toEntity(requestDto, product, loggedInUser, newStock);

        return inventoryMovementMapper.toResponseDto(inventoryMovementRepository.save(inventoryMovement));
    }
}
