package pheninux.xdev.gestork.core.notification.model;

import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;

import java.util.Date;

public class NotificationDto {

    private Long id;
    private Employee waiterId;
    private String message;
    private OrderEntityDto order;
    private boolean isRead;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Employee waiterId) {
        this.waiterId = waiterId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderEntityDto getOrder() {
        return order;
    }

    public void setOrder(OrderEntityDto order) {
        this.order = order;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
