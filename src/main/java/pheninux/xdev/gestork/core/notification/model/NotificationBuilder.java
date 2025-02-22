package pheninux.xdev.gestork.core.notification.model;

import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.order.mapper.OrderMapper;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;

import java.util.Date;

public class NotificationBuilder {

    private Long id;
    private Employee waiterId;
    private String message;
    private OrderEntityDto order;
    private boolean isRead;
    private Date createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void setWaiterId(Employee waiterId) {
        this.waiterId = waiterId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOrder(OrderEntityDto order) {
        this.order = order;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public NotificationDto buildDto() {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(this.id);
        notificationDto.setWaiterId(this.waiterId);
        notificationDto.setMessage(this.message);
        notificationDto.setOrder(this.order);
        notificationDto.setRead(this.isRead);
        notificationDto.setCreatedAt(this.createdAt);
        return notificationDto;
    }

    public Notification buildEntity() {
        Notification notification = new Notification();
        notification.setId(this.id);
        notification.setEmployeeId(this.waiterId);
        notification.setMessage(this.message);
        notification.setOrder(OrderMapper.toEntity(this.order));
        notification.setHasBeenRead(this.isRead);
        notification.setCreatedAt(this.createdAt);
        return notification;
    }


    public static NotificationBuilder fromNotificationEntityToDto(Notification notification) {
        NotificationBuilder notificationBuilder = new NotificationBuilder();
        notificationBuilder.setId(notification.getId());
        notificationBuilder.setWaiterId(notification.getEmployeeId());
        notificationBuilder.setMessage(notification.getMessage());
        notificationBuilder.setOrder(OrderMapper.toDto(notification.getOrder()));
        notificationBuilder.setRead(notification.isHasBeenRead());
        notificationBuilder.setCreatedAt(notification.getCreatedAt());
        return notificationBuilder;
    }

    public static NotificationBuilder fromNotificationDtoToEntity(NotificationDto notificationDto) {
        NotificationBuilder notificationBuilder = new NotificationBuilder();
        notificationBuilder.setId(notificationDto.getId());
        notificationBuilder.setWaiterId(notificationDto.getWaiterId());
        notificationBuilder.setMessage(notificationDto.getMessage());
        notificationBuilder.setOrder(notificationDto.getOrder());
        notificationBuilder.setRead(notificationDto.isRead());
        notificationBuilder.setCreatedAt(notificationDto.getCreatedAt());
        return notificationBuilder;
    }

}
