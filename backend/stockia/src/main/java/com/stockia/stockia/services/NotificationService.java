package com.stockia.stockia.services;

import com.stockia.stockia.dtos.notifications.NotificationFilterDto;
import com.stockia.stockia.dtos.notifications.NotificationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {

    Page<NotificationResponseDto> getUserNotifications(UUID userId, NotificationFilterDto filter, Pageable pageable);

    /**
     * Marca una notificación como leída para el usuario autenticado.
     * Crea un registro en user_notification_read.
     * Es idempotente: si ya está marcada como leída, no hace nada.
     */
    NotificationResponseDto markAsRead(UUID notificationId, UUID userId);

    /**
     * Marca todas las notificaciones como leídas para el usuario autenticado.
     */
    void markAllAsRead(UUID userId);

    void deleteNotification(UUID notificationId);

    Long getUnreadCount(UUID userId);
}
