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

    private final UUID productId;

    private final String productName;

    private final Integer currentStock;

    private final Integer minStock;
}
