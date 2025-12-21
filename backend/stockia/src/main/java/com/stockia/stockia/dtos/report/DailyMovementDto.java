package com.stockia.stockia.dtos.report;

import com.stockia.stockia.enums.MovementType;

import java.time.LocalDate;

/**
 * DTO intermedio para agrupar movimientos diarios por tipo.
 * Usado internamente por las queries JPQL.
 */
public record DailyMovementDto(
        LocalDate date,
        MovementType movementType,
        Long quantity) {
}
