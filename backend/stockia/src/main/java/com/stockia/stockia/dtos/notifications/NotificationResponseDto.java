package com.stockia.stockia.dtos.notifications;

import com.stockia.stockia.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para notificaciones.
 * Incluye el estado de lectura específico del usuario que realiza la consulta.
 */
@Schema(description = "Datos de respuesta de una notificación con estado de lectura del usuario")
public record NotificationResponseDto(
                @Schema(description = "ID único de la notificación", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,

                @Schema(description = "Titulo de la notificación", example = "¡Alerta, se agotó el stock!") String title,

                @Schema(description = "Mensaje descriptivo de la notificación", example = "El producto 'Laptop HP' quedo sin stock. Contactate con tu proveedor para evitar perdida de ventas.") String message,

                @Schema(description = "Tipo de notificación", example = "LOW_STOCK") NotificationType type,

                @Schema(description = "ID del recurso relacionado (ej: producto)", example = "123e4567-e89b-12d3-a456-426614174001") UUID referenceId,

                @Schema(description = "Nombre del recurso relacionado (ej: nombre del producto)", example = "Laptop HP") String referenceName,

                @Schema(description = "URL de la imagen del recurso", example = "https://example.com/images/laptop.jpg") String photoUrl,

                @Schema(description = "Indica si el usuario autenticado ha leído esta notificación", example = "false") Boolean isRead,

                @Schema(description = "Fecha y hora en que el usuario autenticado leyó la notificación", example = "2025-12-01T10:30:00") LocalDateTime readAt,

                @Schema(description = "Fecha y hora de creación de la notificación", example = "2025-12-01T09:15:00") LocalDateTime createdAt) {
}
