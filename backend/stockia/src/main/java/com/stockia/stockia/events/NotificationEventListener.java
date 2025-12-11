package com.stockia.stockia.events;

import com.stockia.stockia.enums.NotificationType;
import com.stockia.stockia.models.Notification;
import com.stockia.stockia.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 * Listener de eventos de notificaciones.
 * Procesa eventos de stock bajo y envía notificaciones.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Maneja eventos de stock bajo.
     * Crea notificación en BD y la envía por WebSocket.
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLowStockEvent(LowStockEvent event) {
        log.info("Procesando evento de stock bajo para producto: {} (ID: {})",
                event.getProductName(), event.getProductId());

        // Determinar el tipo de notificación
        NotificationType type = event.getCurrentStock() == 0
                ? NotificationType.OUT_OF_STOCK
                : NotificationType.LOW_STOCK;

        // Construir mensaje descriptivo
        String message = buildStockMessage(event, type);

        // Crear notificación global en BD
        Notification notification = Notification.builder()
                .message(message)
                .type(type)
                .referenceId(event.getProductId())
                .referenceName(event.getProductName())
                .build();

        notification = notificationRepository.save(notification);
        log.info("Notificación de {} creada con ID: {}", type, notification.getId());

        // Enviar notificación en tiempo real a TODOS los usuarios conectados
        try {
            messagingTemplate.convertAndSend("/topic/notifications", new NotificationWebSocketMessage(
                    notification.getId(),
                    notification.getMessage(),
                    notification.getType(),
                    notification.getReferenceId(),
                    notification.getReferenceName(),
                    notification.getCreatedAt()));
            log.info("Notificación enviada vía WebSocket a /topic/notifications");
        } catch (Exception e) {
            log.error("Error al enviar notificación vía WebSocket: {}", e.getMessage(), e);
            // No lanzamos la excepción para no afectar la transacción principal
        }
    }

    /**
     * Construye el mensaje de la notificación según el tipo.
     */
    private String buildStockMessage(LowStockEvent event, NotificationType type) {
        if (type == NotificationType.OUT_OF_STOCK) {
            return String.format(
                    "¡Alerta, se agotó el stock! - El producto '%s' quedo sin stock. Contactate con tu proveedor para evitar perdida de ventas.",
                    event.getProductName());
        } else {
            return String.format(
                    "¡Alerta, stock bajo! - El producto '%s' alcanzo la cantidad mínima indicada. Contáctate con tu proveedor para evitar faltas.",
                    event.getProductName());
        }
    }

    /**
     * DTO para mensajes WebSocket de notificaciones.
     */
    private record NotificationWebSocketMessage(
            java.util.UUID id,
            String message,
            NotificationType type,
            java.util.UUID referenceId,
            String referenceName,
            java.time.LocalDateTime createdAt) {
    }
}
