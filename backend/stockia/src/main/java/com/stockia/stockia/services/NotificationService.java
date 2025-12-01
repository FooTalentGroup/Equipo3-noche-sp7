package com.stockia.stockia.services;

import com.stockia.stockia.dtos.notifications.NotificationFilterDto;
import com.stockia.stockia.dtos.notifications.NotificationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {

    /**
     * Obtiene las notificaciones con el estado de lectura específico del usuario
     * autenticado.
     *
     * @param userId   ID del usuario autenticado
     * @param filter   filtros opcionales para la búsqueda
     * @param pageable configuración de paginación
     * @return página de notificaciones con estado de lectura del usuario
     */
    Page<NotificationResponseDto> getUserNotifications(UUID userId, NotificationFilterDto filter, Pageable pageable);

    /**
     * Marca una notificación como leída para el usuario autenticado.
     * Crea un registro en user_notification_read.
     * Es idempotente: si ya está marcada como leída, no hace nada.
     *
     * @param notificationId ID de la notificación
     * @param userId         ID del usuario autenticado
     * @return DTO de la notificación actualizada
     */
    NotificationResponseDto markAsRead(UUID notificationId, UUID userId);

    /**
     * Marca todas las notificaciones como leídas para el usuario autenticado.
     *
     * @param userId ID del usuario autenticado
     */
    void markAllAsRead(UUID userId);

    /**
     * Elimina una notificación (soft delete).
     * La eliminación afecta a todos los usuarios.
     *
     * @param notificationId ID de la notificación a eliminar
     */
    void deleteNotification(UUID notificationId);

    /**
     * Cuenta las notificaciones no leídas por el usuario autenticado.
     *
     * @param userId ID del usuario autenticado
     * @return cantidad de notificaciones no leídas
     */
    Long getUnreadCount(UUID userId);
}
