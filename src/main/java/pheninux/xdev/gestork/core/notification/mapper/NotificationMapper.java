package pheninux.xdev.gestork.core.notification.mapper;

import pheninux.xdev.gestork.core.notification.model.Notification;
import pheninux.xdev.gestork.core.notification.model.NotificationBuilder;
import pheninux.xdev.gestork.core.notification.model.NotificationDto;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationMapper {

    public static NotificationDto toDto(Notification notification) {
        return NotificationBuilder.fromNotificationEntityToDto(notification).buildDto();
    }

    public static Notification toEntity(NotificationDto notificationDto) {
        return NotificationBuilder.fromNotificationDtoToEntity(notificationDto).buildEntity();
    }

    public static List<NotificationDto> toDtoList(List<Notification> notifications) {
        return notifications.stream().map(NotificationMapper::toDto).collect(Collectors.toList());
    }


}
