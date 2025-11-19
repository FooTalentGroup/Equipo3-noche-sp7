package com.stockia.stockia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockia.stockia.dtos.InventoryMovementRequestDto;
import com.stockia.stockia.models.InventoryMovement;
import com.stockia.stockia.services.InventoryMovementService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/*
 * Expone el endpoint POST /inventory/movements
 * Recibe el DTO y delega el servicio
 */
@RestController
@RequestMapping("/inventory/movements")
public class InventoryMovementController {
    private final InventoryMovementService movementService;

    public InventoryMovementController(InventoryMovementService movementService){
        this.movementService=movementService;
    }

    @PostMapping
    public ResponseEntity<InventoryMovement> registerMovement(@RequestBody InventoryMovementRequestDto dto) {
        //TODO: process POST request
        InventoryMovement movement = movementService.registerMovement(dto);
        return ResponseEntity.ok(movement);
    }
    
}