package com.stockia.stockia.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Evento que se dispara cuando el stock de un producto llega a un nivel bajo o
 * igual al mínimo.
 * Este evento se procesa después de confirmar la transacción de inventario.
 */
@Getter
@AllArgsConstructor
public class LowStockEvent {
    /**
     * ID del producto con stock bajo
     */
    private final UUID productId;

    /**
     * Nombre del producto
     */
    private final String productName;

    /**
     * Stock actual del producto
     */
    private final Integer currentStock;

    /**
     * Stock mínimo configurado
     */
    private final Integer minStock;
}
