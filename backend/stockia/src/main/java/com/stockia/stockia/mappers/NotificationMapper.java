package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.notifications.NotificationResponseDto;
import com.stockia.stockia.models.Notification;
import com.stockia.stockia.models.UserNotificationRead;
import com.stockia.stockia.utils.NotificationMessageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    /**
     * Convierte una notificación a DTO sin información de lectura.
     *
     * @param notification notificación a convertir
     * @return DTO de respuesta
     */
    @Mapping(target = "isRead", constant = "false")
    @Mapping(target = "readAt", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    NotificationResponseDto toResponseDto(Notification notification);

    /**
     * Convierte una notificación a DTO con información de lectura del usuario.
     *
     * @param notification notificación a convertir
     * @param readRecord   registro de lectura del usuario (puede ser null)
     * @param photoUrl     Imagen del recurso
     * @return DTO de respuesta con estado de lectura
     */
    @Mapping(target = "id", source = "notification.id")
    @Mapping(target = "title",
            expression = "java(com.stockia.stockia.utils.NotificationMessageUtils.extractTitle(notification.getMessage()))")
    @Mapping(target = "message",
            expression = "java(com.stockia.stockia.utils.NotificationMessageUtils.extractMessage(notification.getMessage()))")
    @Mapping(target = "type", source = "notification.type")
    @Mapping(target = "referenceId", source = "notification.referenceId")
    @Mapping(target = "referenceName", source = "notification.referenceName")
    @Mapping(target = "photoUrl", source = "photoUrl")
    @Mapping(target = "createdAt", source = "notification.createdAt")
    @Mapping(target = "isRead", expression = "java(readRecord != null)")
    @Mapping(target = "readAt", source = "readRecord.readAt")
    NotificationResponseDto toResponseDto(Notification notification, UserNotificationRead readRecord,
                                          String photoUrl);
}
