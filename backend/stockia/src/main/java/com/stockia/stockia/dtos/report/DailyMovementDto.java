package com.stockia.stockia.dtos.report;

import com.stockia.stockia.enums.MovementType;

import java.time.LocalDate;

/**
 * DTO intermedio para agrupar movimientos diarios por tipo.
 * Usado internamente por las queries JPQL.
 *
 * @param date         Fecha del movimiento
 * @param movementType Tipo de movimiento (IN/OUT)
 * @param quantity     Cantidad total del tipo en ese día
 */
public record DailyMovementDto(
        LocalDate date,
        MovementType movementType,
        Long quantity) {
}
