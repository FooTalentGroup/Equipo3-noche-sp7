package com.stockia.stockia.repositories;

import com.stockia.stockia.models.UserNotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad UserNotificationRead.
 * Gestiona el tracking de qué usuarios han leído cada notificación.
 */
@Repository
public interface UserNotificationReadRepository extends JpaRepository<UserNotificationRead, UUID> {

    /**
     * Busca el registro de lectura de una notificación por un usuario específico.
     *
     * @param userId         ID del usuario
     * @param notificationId ID de la notificación
     * @return Optional con el registro si existe
     */
    Optional<UserNotificationRead> findByUserIdAndNotificationId(UUID userId, UUID notificationId);

    /**
     * Obtiene todos los registros de lectura de un usuario.
     *
     * @param userId ID del usuario
     * @return lista de registros de lectura
     */
    List<UserNotificationRead> findByUserId(UUID userId);

    /**
     * Cuenta cuántas notificaciones ha leído un usuario.
     *
     * @param userId ID del usuario
     * @return cantidad de notificaciones leídas
     */
    long countByUserId(UUID userId);

    /**
     * Verifica si un usuario ya leyó una notificación específica.
     *
     * @param userId         ID del usuario
     * @param notificationId ID de la notificación
     * @return true si existe el registro, false en caso contrario
     */
    boolean existsByUserIdAndNotificationId(UUID userId, UUID notificationId);

    /**
     * Obtiene los IDs de las notificaciones leídas por un usuario.
     *
     * @param userId ID del usuario
     * @return lista de IDs de notificaciones leídas
     */
    @Query("SELECT unr.notification.id FROM UserNotificationRead unr WHERE unr.user.id = :userId")
    List<UUID> findReadNotificationIdsByUserId(@Param("userId") UUID userId);
}
