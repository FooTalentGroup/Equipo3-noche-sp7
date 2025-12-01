package com.stockia.stockia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que registra qué usuarios han leído cada notificación.
 * Permite que cada usuario tenga su propio estado de lectura para una
 * notificación global.
 */
@Entity
@Table(name = "user_notification_read", uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_notification", columnNames = { "user_id", "notification_id" })
}, indexes = {
                @Index(name = "idx_user_notif_read", columnList = "user_id, notification_id"),
                @Index(name = "idx_user_read_at", columnList = "user_id, read_at DESC")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "user", "notification" })
@EqualsAndHashCode(of = { "id" })
@EntityListeners(AuditingEntityListener.class)
public class UserNotificationRead {

        /**
         * Identificador único del registro.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        /**
         * Usuario que leyó la notificación.
         */
        @NotNull(message = "El usuario es obligatorio")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        /**
         * Notificación que fue leída.
         */
        @NotNull(message = "La notificación es obligatoria")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "notification_id", nullable = false)
        private Notification notification;

        /**
         * Fecha y hora en que el usuario marcó la notificación como leída.
         * Se establece automáticamente al crear el registro.
         */
        @CreatedDate
        @Column(name = "read_at", nullable = false, updatable = false)
        private LocalDateTime readAt;
}
