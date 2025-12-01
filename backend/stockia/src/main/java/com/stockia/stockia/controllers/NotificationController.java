package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.notification.DeleteNotificationDoc;
import com.stockia.stockia.documentation.notification.GetNotificationsDoc;
import com.stockia.stockia.documentation.notification.GetUnreadCountDoc;
import com.stockia.stockia.documentation.notification.MarkAllAsReadDoc;
import com.stockia.stockia.documentation.notification.MarkAsReadDoc;
import com.stockia.stockia.dtos.notifications.NotificationFilterDto;
import com.stockia.stockia.dtos.notifications.NotificationResponseDto;
import com.stockia.stockia.enums.NotificationType;
import com.stockia.stockia.security.CustomUserDetails;
import com.stockia.stockia.services.NotificationService;
import com.stockia.stockia.utils.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para gestionar notificaciones del sistema.
 * 
 * Endpoints disponibles:
 * 
 * - GET [/api/notifications] → Listar notificaciones con paginación
 * - GET [/api/notifications/unread-count] → Obtener contador de no leídas
 * - PUT [/api/notifications/{id}/read] → Marcar como leída
 * - PUT [/api/notifications/read-all] → Marcar todas como leídas
 * - DELETE [/api/notifications/{id}] → Eliminar notificación (admin)
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "07 - Notificaciones", description = "Endpoints para gestionar notificaciones en tiempo real")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Lista las notificaciones con el estado de lectura del usuario autenticado.
     * 
     * Permite filtrar por:
     * - type: Tipo de notificación (LOW_STOCK, OUT_OF_STOCK, SALE_SUCCESS, ERROR)
     * - isRead: Estado de lectura (true = leídas, false = no leídas)
     * - referenceId: ID del producto relacionado
     * 
     * Ejemplos de uso:
     * - GET /api/notifications?page=0&size=20
     * - GET /api/notifications?isRead=false&page=0
     * - GET /api/notifications?type=LOW_STOCK&page=0&size=10
     *
     * @param userDetails usuario autenticado
     * @param type        filtro opcional por tipo de notificación
     * @param isRead      filtro opcional por estado de lectura
     * @param referenceId filtro opcional por ID de referencia
     * @param pageable    configuración de paginación
     * @return página de notificaciones
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @GetNotificationsDoc
    public ResponseEntity<ApiResult<Page<NotificationResponseDto>>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) UUID referenceId,
            @org.springdoc.core.annotations.ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        NotificationFilterDto filter = new NotificationFilterDto(type, isRead, referenceId);
        Page<NotificationResponseDto> notifications = notificationService.getUserNotifications(
                userDetails.getId(),
                filter,
                pageable);

        String message = notifications.isEmpty()
                ? "No se encontraron notificaciones"
                : String.format("%d notificación(es) encontrada(s)", notifications.getTotalElements());

        return ResponseEntity.ok(ApiResult.success(message, notifications));
    }

    /**
     * Obtiene la cantidad de notificaciones no leídas por el usuario autenticado.
     *
     * @param userDetails usuario autenticado
     * @return contador de notificaciones no leídas
     */
    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    @GetUnreadCountDoc
    public ResponseEntity<ApiResult<Long>> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long count = notificationService.getUnreadCount(userDetails.getId());
        String message = count == 0
                ? "No hay notificaciones pendientes"
                : String.format("Tienes %d notificación(es) sin leer", count);
        return ResponseEntity.ok(ApiResult.success(message, count));
    }

    /**
     * Marca una notificación como leída para el usuario autenticado.
     *
     * @param id          ID de la notificación
     * @param userDetails usuario autenticado
     * @return notificación actualizada
     */
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    @MarkAsReadDoc
    public ResponseEntity<ApiResult<NotificationResponseDto>> markAsRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        NotificationResponseDto notification = notificationService.markAsRead(id, userDetails.getId());
        return ResponseEntity.ok(ApiResult.success("Notificación marcada como leída", notification));
    }

    /**
     * Marca todas las notificaciones como leídas para el usuario autenticado.
     *
     * @param userDetails usuario autenticado
     * @return respuesta exitosa
     */
    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    @MarkAllAsReadDoc
    public ResponseEntity<ApiResult<Void>> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok(ApiResult.success("Todas las notificaciones han sido marcadas como leídas"));
    }

    /**
     * Elimina una notificación (soft delete).
     * La eliminación afecta a todos los usuarios.
     * Solo administradores pueden eliminar notificaciones.
     *
     * @param id ID de la notificación
     * @return respuesta exitosa
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteNotificationDoc
    public ResponseEntity<ApiResult<Void>> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResult.success("Notificación eliminada exitosamente"));
    }
}
