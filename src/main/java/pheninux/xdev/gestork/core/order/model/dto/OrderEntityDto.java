package pheninux.xdev.gestork.core.order.model.dto;

import pheninux.xdev.gestork.core.order.model.OrderStatus;

import java.util.Date;
import java.util.List;

public class OrderEntityDto {

    private Long orderId;
    private String customerLogin;
    private Long waiterId; // ID de l'employé (waiter)
    private Integer tableNumber;
    private double totalAmount;
    private Date orderDate;
    private String orderType; // Peut être une chaîne ou un Enum
    private Long orderDetailsId; // ID des détails de la commande
    private List<OrderDishesDto> orderDishesDto; // Liste des plats commandés
    private OrderStatus orderStatus; // Indique si la commande a été payée

    // Getters et Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerLogin() {
        return customerLogin;
    }

    public void setCustomerLogin(String customerLogin) {
        this.customerLogin = customerLogin;
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Long orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean waiterApproved() {
        return orderStatus == OrderStatus.VALIDATE;
    }

    public List<OrderDishesDto> getOrderDishesDto() {
        return orderDishesDto;
    }

    public void setOrderDishesDto(List<OrderDishesDto> orderDishesDto) {
        this.orderDishesDto = orderDishesDto;
    }
}