package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.notifications.NotificationFilterDto;
import com.stockia.stockia.dtos.notifications.NotificationResponseDto;
import com.stockia.stockia.exceptions.notification.NotificationNotFoundException;
import com.stockia.stockia.exceptions.user.UserNotFoundException;
import com.stockia.stockia.mappers.NotificationMapper;
import com.stockia.stockia.models.Notification;
import com.stockia.stockia.models.User;
import com.stockia.stockia.models.UserNotificationRead;
import com.stockia.stockia.repositories.NotificationRepository;
import com.stockia.stockia.repositories.ProductRepository;
import com.stockia.stockia.repositories.UserNotificationReadRepository;
import com.stockia.stockia.repositories.UserRepository;
import com.stockia.stockia.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

        private final NotificationRepository notificationRepository;
        private final UserNotificationReadRepository readRepository;
        private final UserRepository userRepository;
        private final ProductRepository productRepository;
        private final NotificationMapper notificationMapper;

        @Override
        @Transactional(readOnly = true)
        public Page<NotificationResponseDto> getUserNotifications(UUID userId, NotificationFilterDto filter,
                        Pageable pageable) {
                Page<Notification> notifications;

                if (filter != null && (filter.type() != null || filter.referenceId() != null)) {
                        notifications = notificationRepository.searchNotifications(
                                        filter.type(),
                                        filter.referenceId(),
                                        pageable);
                } else {
                        notifications = notificationRepository.findByDeletedFalseOrderByCreatedAtDesc(pageable);
                }

                List<UUID> readNotificationIds = readRepository.findReadNotificationIdsByUserId(userId);

                List<Notification> filteredNotifications = notifications.getContent();
                if (filter != null && filter.isRead() != null) {
                        filteredNotifications = filteredNotifications.stream()
                                        .filter(n -> filter.isRead().equals(readNotificationIds.contains(n.getId())))
                                        .toList();
                }

                List<UUID> notificationIds = filteredNotifications.stream()
                                .map(Notification::getId)
                                .toList();

                List<UserNotificationRead> readRecords = notificationIds.isEmpty()
                                ? List.of()
                                : readRepository.findByUserId(userId).stream()
                                                .filter(r -> notificationIds.contains(r.getNotification().getId()))
                                                .toList();

                Map<UUID, UserNotificationRead> readRecordMap = readRecords.stream()
                                .collect(Collectors.toMap(
                                                r -> r.getNotification().getId(),
                                                Function.identity()));

                List<NotificationResponseDto> dtos = filteredNotifications.stream()
                                .map(notification -> {
                                        String photoUrl = null;
                                        if (notification.getReferenceId() != null) {
                                                photoUrl = productRepository.findPhotoUrlById(notification.getReferenceId());
                                        }
                                        return notificationMapper.toResponseDto(
                                                notification,
                                                readRecordMap.get(notification.getId()), photoUrl);})
                                .toList();

                return new PageImpl<>(dtos, pageable, notifications.getTotalElements());
        }

        @Override
        @Transactional
        public NotificationResponseDto markAsRead(UUID notificationId, UUID userId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(
                                                () -> new NotificationNotFoundException(
                                                                "Notificación no encontrada con ID: "
                                                                                + notificationId));

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no encontrado con ID: " + userId));
                if (readRepository.existsByUserIdAndNotificationId(userId, notificationId)) {
                        log.debug("La notificación {} ya fue marcada como leída por el usuario {}", notificationId,
                                        userId);
                        UserNotificationRead existingRead = readRepository
                                        .findByUserIdAndNotificationId(userId, notificationId)
                                        .orElseThrow();
                        return notificationMapper.toResponseDto(notification,
                                existingRead,
                                productRepository.findPhotoUrlById(notification.getReferenceId())
                        );
                }

                UserNotificationRead readRecord = UserNotificationRead.builder()
                                .user(user)
                                .notification(notification)
                                .build();

                readRecord = readRepository.save(readRecord);
                log.info("Notificación {} marcada como leída por usuario {}", notificationId, userId);

                return notificationMapper.toResponseDto(
                        notification,
                        readRecord,
                        productRepository.findPhotoUrlById(notification.getReferenceId())
                        );
        }

        @Override
        @Transactional
        public void markAllAsRead(UUID userId) {
                userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no encontrado con ID: " + userId));
                List<Notification> allNotifications = notificationRepository
                                .findByDeletedFalseOrderByCreatedAtDesc(Pageable.unpaged()).getContent();

                List<UUID> readNotificationIds = readRepository.findReadNotificationIdsByUserId(userId);

                List<UserNotificationRead> newReadRecords = allNotifications.stream()
                                .filter(n -> !readNotificationIds.contains(n.getId()))
                                .map(notification -> UserNotificationRead.builder()
                                                .user(userRepository.findById(userId).orElseThrow())
                                                .notification(notification)
                                                .build())
                                .toList();

                if (!newReadRecords.isEmpty()) {
                        readRepository.saveAll(newReadRecords);
                        log.info("Marcadas {} notificaciones como leídas para usuario {}", newReadRecords.size(),
                                        userId);
                }
        }

        @Override
        @Transactional
        public void deleteNotification(UUID notificationId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(
                                                () -> new NotificationNotFoundException(
                                                                "Notificación no encontrada con ID: "
                                                                                + notificationId));

                notification.markAsDeleted();
                notificationRepository.save(notification);
                log.info("Notificación {} marcada como eliminada", notificationId);
        }

        @Override
        @Transactional(readOnly = true)
        public Long getUnreadCount(UUID userId) {
                long totalNotifications = notificationRepository
                                .findByDeletedFalseOrderByCreatedAtDesc(Pageable.unpaged())
                                .getTotalElements();
                long readNotifications = readRepository.findReadNotificationIdsByUserId(userId).size();

                return totalNotifications - readNotifications;
        }
}
