package com.stockia.stockia.services;

import org.springframework.stereotype.Service;

import com.stockia.stockia.dtos.InventoryMovementRequestDto;
import com.stockia.stockia.models.InventoryMovement;
import com.stockia.stockia.repositories.InventoryMovementRepository;
import com.stockia.stockia.repositories.UserRepository;

/*
 * Busca el producto y usuario por ID.
 * Si el movimiento es de tipo IN, actualiza el stock.
 * Guarda el movimiento en la base.
 */

@Service
public class InventoryMovementService {
    private final InventoryMovementRepository movementRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public InventoryMovementService(InventoryMovementRepository movementRepo,
    ProductRespository productRepo,UserRepository userRepo){
        this.movementRepo=movementRepo;
        this.productRepo=productRepo;
        this.userRepo=userRepo;
    }

    public InventoryMovement registerMovement(InventoryMovementRequestDto dto){
        Product product = productRepo.findById(dto.getProductId()).
        orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        User user = userReppo.findById(dto.getUserId()).
        orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setUser(user);
        movement.setMovementType(dto.getMovementType());
        movement.setQuantity(dto.getQuantity());
        movement.setReason(dto.getReason());

        if("IN".equals(dto.getMovementType())){
            product.setCurrentStock(product.getCurrentStock()+dto.getQuantity());
            productRepo.save(product);
        }
        return movementRepo.save(movement);
    }
}
