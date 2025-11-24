package com.stockia.stockia.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockia.stockia.dtos.InventoryMovementRequestDto;
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
import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * Servicio para gestionar movimientos de inventario.
 * Busca el producto y usuario por ID.
 * Actualiza el stock según el tipo de movimiento.
 * Guarda el movimiento en la base de datos.
 * El usuario se obtiene automáticamente del contexto de seguridad.
 */
@Service
public class InventoryMovementService {
    private final InventoryMovementRepository movementRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public InventoryMovementService(InventoryMovementRepository movementRepo,
            ProductRepository productRepo,
            UserRepository userRepo) {
        this.movementRepo = movementRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public InventoryMovement registerMovement(InventoryMovementRequestDto dto, Authentication authentication) {
        // Obtener el usuario autenticado del contexto de seguridad
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID authenticatedUserId = userDetails.getId();

        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(dto.getProductId()));

        User user = userRepo.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + authenticatedUserId));

        // Calcular el nuevo stock según el tipo de movimiento
        int newStock = product.getCurrentStock();
        if (dto.getMovementType() == MovementType.IN) {
            newStock += dto.getQuantity();
        } else if (dto.getMovementType() == MovementType.OUT) {
            newStock -= dto.getQuantity();
        } else if (dto.getMovementType() == MovementType.ADJUSTMENT) {
            newStock = dto.getQuantity(); // Para ajustes, quantity es el nuevo valor absoluto
        }

        // Validar que el stock no sea negativo
        if (newStock < 0) {
            throw new InsufficientStockException(product.getCurrentStock(), Math.abs(dto.getQuantity()));
        }

        // Crear el movimiento
        InventoryMovement movement = InventoryMovement.builder()
                .product(product)
                .user(user)
                .movementType(dto.getMovementType())
                .quantity(dto.getQuantity())
                .reason(dto.getReason())
                .newStock(newStock)
                .purchaseCost(dto.getPurchaseCost())
                .build();

        // Actualizar el stock del producto
        product.setCurrentStock(newStock);
        productRepo.save(product);

        // Guardar el movimiento
        return movementRepo.save(movement);
    }
}
