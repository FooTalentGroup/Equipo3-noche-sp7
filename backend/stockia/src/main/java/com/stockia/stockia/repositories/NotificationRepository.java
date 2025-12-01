package com.stockia.stockia.repositories;

import com.stockia.stockia.enums.NotificationType;
import com.stockia.stockia.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositorio para la entidad Notification.
 * Proporciona métodos para consultar notificaciones globales del sistema.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

        /**
         * Obtiene todas las notificaciones no eliminadas ordenadas por fecha de
         * creación descendente.
         *
         * @param pageable configuración de paginación
         * @return página de notificaciones
         */
        Page<Notification> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

        /**
         * Filtra notificaciones por tipo, excluyendo las eliminadas.
         *
         * @param type     tipo de notificación
         * @param pageable configuración de paginación
         * @return página de notificaciones filtradas
         */
        Page<Notification> findByTypeAndDeletedFalseOrderByCreatedAtDesc(NotificationType type, Pageable pageable);

        /**
         * Filtra notificaciones por ID de referencia (ej: producto), excluyendo las
         * eliminadas.
         *
         * @param referenceId ID del recurso relacionado
         * @param pageable    configuración de paginación
         * @return página de notificaciones filtradas
         */
        Page<Notification> findByReferenceIdAndDeletedFalseOrderByCreatedAtDesc(UUID referenceId, Pageable pageable);

        /**
         * Busca notificaciones con criterios dinámicos.
         *
         * @param type        tipo de notificación (opcional)
         * @param referenceId ID de referencia (opcional)
         * @param pageable    configuración de paginación
         * @return página de notificaciones filtradas
         */
        @Query("SELECT n FROM Notification n WHERE n.deleted = false " +
                        "AND (:type IS NULL OR n.type = :type) " +
                        "AND (:referenceId IS NULL OR n.referenceId = :referenceId) " +
                        "ORDER BY n.createdAt DESC")
        Page<Notification> searchNotifications(
                        @Param("type") NotificationType type,
                        @Param("referenceId") UUID referenceId,
                        Pageable pageable);
}
