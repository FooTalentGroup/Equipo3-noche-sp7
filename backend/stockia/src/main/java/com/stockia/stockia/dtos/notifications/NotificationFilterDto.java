package com.stockia.stockia.dtos.notifications;

import com.stockia.stockia.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Criterios de filtrado para notificaciones")
public record NotificationFilterDto(
                @Schema(description = "Filtrar por tipo de notificación", example = "LOW_STOCK", nullable = true) NotificationType type,

                @Schema(description = "Filtrar por estado de lectura (true = leídas, false = no leídas)", example = "false", nullable = true) Boolean isRead,

                @Schema(description = "Filtrar por ID de referencia (ej: ID del producto)", example = "123e4567-e89b-12d3-a456-426614174001", nullable = true) UUID referenceId) {
}
