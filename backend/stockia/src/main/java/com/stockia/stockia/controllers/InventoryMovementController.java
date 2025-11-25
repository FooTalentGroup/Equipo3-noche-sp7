package com.stockia.stockia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockia.stockia.documentation.inventory.RegisterInventoryMovementEndpointDoc;
import com.stockia.stockia.dtos.InventoryMovementRequestDto;
import com.stockia.stockia.models.InventoryMovement;
import com.stockia.stockia.services.InventoryMovementService;
import com.stockia.stockia.utils.ApiResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador REST para la gestión de movimientos de inventario.
 * Expone endpoints para registrar entradas, salidas y ajustes de stock.
 */
@RestController
@RequestMapping("/inventory/movements")
@RequiredArgsConstructor
@Tag(name = "02 - Movimientos", description = "Endpoints para la gestión de movimientos de inventario")
public class InventoryMovementController {
    private final InventoryMovementService movementService;

    @RegisterInventoryMovementEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResult<?>> registerMovement(
            @RequestBody @Valid InventoryMovementRequestDto dto,
            Authentication authentication) {
        InventoryMovement movement = movementService.registerMovement(dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(movement, "Movimiento de inventario registrado exitosamente."));
    }
}