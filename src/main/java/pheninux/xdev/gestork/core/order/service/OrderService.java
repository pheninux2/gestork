package pheninux.xdev.gestork.core.order.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.core.notification.model.Notification;
import pheninux.xdev.gestork.core.notification.service.NotificationService;
import pheninux.xdev.gestork.core.order.mapper.OrderMapper;
import pheninux.xdev.gestork.core.order.model.OrderEntity;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;
import pheninux.xdev.gestork.core.order.repository.OrderRepository;
import pheninux.xdev.gestork.response.CustomResponseBody;
import pheninux.xdev.gestork.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final DishService dishService;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, DishService dishService,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.dishService = dishService;
        this.notificationService = notificationService;
    }


    public CustomResponseBody<OrderEntityDto> createOrder(OrderEntity orderEntity) {
        try {
            List<Dish> orderDishes = new ArrayList<>();

            for (Dish dish : orderEntity.getOrderDetails().getOrderedDishes()) {
                Dish dishEntity = dishService.findById(dish.getDishId());
                orderDishes.add(dishEntity);
            }
            orderEntity.getOrderDetails().setOrderedDishes(orderDishes);
            OrderEntity order = orderRepository.save(orderEntity);
            OrderEntityDto orderEntityDto = OrderMapper.toDto(order);
            String alertMessage = Utils.renderAlertSingle("alert-success",
                    "Order created successfully");


            Notification notification = new Notification(orderEntity.getWaiter(),
                    "New order created",
                    orderEntity,
                    false,
                    new Date());

            notificationService.createNotification(notification);

            return new CustomResponseBody<>(orderEntityDto,
                    "Order created successfully",
                    200,
                    alertMessage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new CustomResponseBody<>(null,
                    e.getMessage(),
                    500,
                    Utils.renderAlertSingle("alert-danger",
                            "Error while creating order"));
        }
    }

    public void updateOrder() {

    }

    public void deleteOrder() {

    }

    public OrderEntityDto getOrderById(Long id) {

        return new OrderEntityDto();
    }

    public List<OrderEntityDto> getAllOrders() {
        return null;
    }
}
