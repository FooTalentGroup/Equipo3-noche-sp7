package com.stockia.stockia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockia.stockia.services.InventoryMovementService;

@RestController
@RequestMapping("/inventory/movements")
public class InventoryMovementController {
    private final InventoryMovementService movementService;
}