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
import com.stockia.stockia.events.LowStockEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InventoryMovementServiceImpl implements InventoryMovementService {
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryMovementMapper inventoryMovementMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<InventoryMovementResponseDto> searchInventoryMovements(MovementSearchRequestDto params,
            Pageable pageable) {
        LocalDateTime startDateTime = params.startDate() != null
                ? params.startDate().atStartOfDay()
                : null;

        LocalDateTime endDateTime = params.endDate() != null
                ? params.endDate().atTime(23, 59, 59)
                : null;
        Page<InventoryMovement> inventoryMovements = inventoryMovementRepository.searchInventoryMovements(
                params.productId(),
                params.productName(),
                params.movementType(),
                params.userId(),
                startDateTime,
                endDateTime,
                pageable);
        return inventoryMovementMapper.toResponseDto(inventoryMovements);
    }

    @Override
    public InventoryMovementResponseDto findById(UUID id) {
        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Movimiento de inventario no encontrado con ID: " + id));
        return inventoryMovementMapper.toResponseDto(inventoryMovement);
    }

    @Transactional
    @Override
    public InventoryMovementResponseDto registerInventoryMovement(InventoryMovementRequestDto requestDto,
            CustomUserDetails userDetails) {
        Product product = productRepository.findById(requestDto.productId())
                .orElseThrow(
                        () -> new ProductNotFoundException("Producto no encontrado con ID: " + requestDto.productId()));

        User loggedInUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userDetails.getId()));

        if (requestDto.movementType() == MovementType.IN && requestDto.purchaseCost() == null) {
            throw new IllegalArgumentException("El costo de compra es obligatorio para movimientos de entrada");
        }
        if (requestDto.movementType() != MovementType.IN && requestDto.purchaseCost() != null) {
            throw new IllegalArgumentException("El costo de compra solo debe enviarse movimientos de entrada");
        }
        int newStock = switch (requestDto.movementType()) {
            case IN -> product.getCurrentStock() + requestDto.quantity();
            case OUT -> product.getCurrentStock() - requestDto.quantity();
            case ADJUSTMENT -> requestDto.quantity();
        };

        if (newStock < 0) {
            throw new InsufficientStockException(product.getCurrentStock(), Math.abs(requestDto.quantity()));
        }

        product.setCurrentStock(newStock);
        productRepository.save(product);

        if (product.getCurrentStock() <= product.getMinStock()) {
            eventPublisher.publishEvent(new LowStockEvent(
                    product.getId(),
                    product.getName(),
                    product.getCurrentStock(),
                    product.getMinStock()));
        }

        InventoryMovement inventoryMovement = inventoryMovementMapper.toEntity(requestDto, product, loggedInUser,
                newStock);

        return inventoryMovementMapper.toResponseDto(inventoryMovementRepository.save(inventoryMovement));
    }
}
