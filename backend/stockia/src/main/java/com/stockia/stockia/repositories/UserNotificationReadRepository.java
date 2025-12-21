package com.stockia.stockia.repositories;

import com.stockia.stockia.models.UserNotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserNotificationReadRepository extends JpaRepository<UserNotificationRead, UUID> {

    Optional<UserNotificationRead> findByUserIdAndNotificationId(UUID userId, UUID notificationId);

    List<UserNotificationRead> findByUserId(UUID userId);

    long countByUserId(UUID userId);

    boolean existsByUserIdAndNotificationId(UUID userId, UUID notificationId);

    @Query("SELECT unr.notification.id FROM UserNotificationRead unr WHERE unr.user.id = :userId")
    List<UUID> findReadNotificationIdsByUserId(@Param("userId") UUID userId);
}
