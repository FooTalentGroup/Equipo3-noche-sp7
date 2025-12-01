package com.stockia.stockia.models;

import com.stockia.stockia.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una notificación global del sistema.
 * Las notificaciones son compartidas por todos los usuarios, y cada usuario
 * puede marcarlas como leídas de forma independiente mediante
 * UserNotificationRead.
 */
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_created", columnList = "created_at DESC"),
        @Index(name = "idx_notification_type", columnList = "type"),
        @Index(name = "idx_notification_reference", columnList = "reference_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "id" })
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    /**
     * Identificador único de la notificación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Mensaje descriptivo de la notificación que verá el usuario.
     */
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String message;

    /**
     * Tipo de notificación (LOW_STOCK, OUT_OF_STOCK, etc.)
     */
    @NotNull(message = "El tipo de notificación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    /**
     * ID del recurso relacionado (ej: ID del producto).
     * Permite navegar al detalle del producto desde la notificación.
     */
    @Column(name = "reference_id")
    private UUID referenceId;

    /**
     * Nombre del recurso relacionado (ej: nombre del producto).
     * Desnormalizado para eficiencia en consultas.
     */
    @Size(max = 200, message = "El nombre de referencia no puede superar los 200 caracteres")
    @Column(name = "reference_name", length = 200)
    private String referenceName;

    /**
     * Indica si la notificación ha sido eliminada (soft delete).
     * Esto permite mantener historial para auditoría.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    /**
     * Fecha y hora de eliminación de la notificación.
     * Solo se establece cuando deleted = true.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Fecha y hora de creación de la notificación.
     * Se establece automáticamente al crear el registro.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Método de utilidad para marcar la notificación como eliminada (soft delete).
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
