package pheninux.xdev.gestork.core.order.model;

import jakarta.persistence.*;
import pheninux.xdev.gestork.core.delivery.model.DeliveryDetails;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.payment.model.PaymentStatus;

import java.util.List;

@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailsId;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus commandStatus;

    private String comment;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "dish_order", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "order_id"), // Clé étrangère vers Order
            inverseJoinColumns = @JoinColumn(name = "dish_id") // Clé étrangère vers Dish
    )
    private List<Dish> orderedDishes;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_details_id")
    private DeliveryDetails deliveryDetails;


    public Long getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Long orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(OrderStatus commandStatus) {
        this.commandStatus = commandStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Dish> getOrderedDishes() {
        return orderedDishes;
    }

    public void setOrderedDishes(List<Dish> orderedDishes) {
        this.orderedDishes = orderedDishes;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }
}