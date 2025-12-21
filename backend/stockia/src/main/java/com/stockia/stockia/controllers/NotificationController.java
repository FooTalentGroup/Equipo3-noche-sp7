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

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "08 - Notificaciones", description = "Endpoints para gestionar notificaciones en tiempo real")
public class NotificationController {

    private final NotificationService notificationService;

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

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    @MarkAsReadDoc
    public ResponseEntity<ApiResult<NotificationResponseDto>> markAsRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        NotificationResponseDto notification = notificationService.markAsRead(id, userDetails.getId());
        return ResponseEntity.ok(ApiResult.success("Notificación marcada como leída", notification));
    }

    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    @MarkAllAsReadDoc
    public ResponseEntity<ApiResult<Void>> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok(ApiResult.success("Todas las notificaciones han sido marcadas como leídas"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteNotificationDoc
    public ResponseEntity<ApiResult<Void>> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResult.success("Notificación eliminada exitosamente"));
    }
}
