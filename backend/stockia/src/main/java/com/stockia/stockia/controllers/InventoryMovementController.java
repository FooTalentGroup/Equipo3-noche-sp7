package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.inventoryMovement.GetAllMovementsEndpointDoc;
import com.stockia.stockia.documentation.inventoryMovement.GetMovementByIdEndpointDoc;
import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementResponseDto;
import com.stockia.stockia.dtos.inventoryMovements.MovementSearchRequestDto;
import com.stockia.stockia.security.CustomUserDetails;
import com.stockia.stockia.services.InventoryMovementService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.stockia.stockia.documentation.inventoryMovement.RegisterMovementEndpointDoc;
import com.stockia.stockia.dtos.inventoryMovements.InventoryMovementRequestDto;
import com.stockia.stockia.utils.ApiResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * Controlador REST para la gestión de movimientos de inventario.
 * Expone endpoints para registrar entradas, salidas y ajustes de stock.
 */
@RestController
@RequestMapping("/api/inventory-movements")
@RequiredArgsConstructor
@Tag(name = "02 - Movimientos", description = "Endpoints para la gestión de movimientos de inventario")
public class InventoryMovementController {
    private final InventoryMovementService inventoryMovementService;

    @GetAllMovementsEndpointDoc
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllWithSearch(@ParameterObject @Valid MovementSearchRequestDto params,
                                              @ParameterObject Pageable pageable) {
        Page<InventoryMovementResponseDto> response = inventoryMovementService.searchInventoryMovements(params, pageable);
        return ResponseEntity.ok()
                .body(ApiResult.success(response, "Operación exitosa"));
    }

    @GetMovementByIdEndpointDoc
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        InventoryMovementResponseDto response = inventoryMovementService.findById(id);
        return ResponseEntity.ok().body(ApiResult.success(response, "Movimiento de inventario encontrado"));
    }

    @RegisterMovementEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResult<?>> registerInventoryMovement(
            @RequestBody @Valid InventoryMovementRequestDto requestDto,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        InventoryMovementResponseDto registeredMovement = inventoryMovementService.registerInventoryMovement(requestDto, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(registeredMovement, "Movimiento de inventario registrado exitosamente"));
    }
}