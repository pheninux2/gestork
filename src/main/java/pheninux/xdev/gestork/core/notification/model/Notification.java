package pheninux.xdev.gestork.core.notification.model;

import jakarta.persistence.*;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.order.model.OrderEntity;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;

import java.util.Date;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")

    private Employee employeeId;
    private String message;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderEntity order;


    private boolean hasBeenRead;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Notification(Employee employeeId,
                        String message,
                        OrderEntity order,
                        boolean hasBeenRead,
                        Date createdAt) {

        this.employeeId = employeeId;
        this.message = message;
        this.order = order;
        this.hasBeenRead = hasBeenRead;
        this.createdAt = createdAt;
    }

    public Notification() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee waiterId) {
        this.employeeId = waiterId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public boolean isHasBeenRead() {
        return hasBeenRead;
    }

    public void setHasBeenRead(boolean hasBeenRead) {
        this.hasBeenRead = hasBeenRead;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}
