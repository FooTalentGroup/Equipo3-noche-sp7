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

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

        Page<Notification> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

        Page<Notification> findByTypeAndDeletedFalseOrderByCreatedAtDesc(NotificationType type, Pageable pageable);

        Page<Notification> findByReferenceIdAndDeletedFalseOrderByCreatedAtDesc(UUID referenceId, Pageable pageable);

        @Query("SELECT n FROM Notification n WHERE n.deleted = false " +
                        "AND (:type IS NULL OR n.type = :type) " +
                        "AND (:referenceId IS NULL OR n.referenceId = :referenceId) " +
                        "ORDER BY n.createdAt DESC")
        Page<Notification> searchNotifications(
                        @Param("type") NotificationType type,
                        @Param("referenceId") UUID referenceId,
                        Pageable pageable);
}
