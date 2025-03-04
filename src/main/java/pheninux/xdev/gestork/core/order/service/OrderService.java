package pheninux.xdev.gestork.core.order.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;
import pheninux.xdev.gestork.core.accessCode.service.AccessCodeService;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.core.notification.model.Notification;
import pheninux.xdev.gestork.core.notification.service.NotificationService;
import pheninux.xdev.gestork.core.order.mapper.OrderMapper;
import pheninux.xdev.gestork.core.order.model.OrderEntity;
import pheninux.xdev.gestork.core.order.model.OrderStatus;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;
import pheninux.xdev.gestork.core.order.repository.OrderRepository;
import pheninux.xdev.gestork.response.CustomResponseBody;
import pheninux.xdev.gestork.utils.Utils;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final DishService dishService;
    private final NotificationService notificationService;
    private final AccessCodeService accessCodeService;

    public OrderService(OrderRepository orderRepository, DishService dishService,
                        NotificationService notificationService, AccessCodeService accessCodeService) {
        this.orderRepository = orderRepository;
        this.dishService = dishService;
        this.notificationService = notificationService;
        this.accessCodeService = accessCodeService;
    }


    public CustomResponseBody<OrderEntityDto> createOrder(OrderEntity orderEntity, String code) {
        try {
            // List<Dish> dishes = new ArrayList<>();

            if (!accessCodeService.isAccessCodeValid(code)) {
                return new CustomResponseBody<>(null,
                        "Invalid code",
                        500,
                        Utils.renderAlertSingle("alert-danger",
                                "Invalid code"));
            } else {
                enrichOrderEntityWithCode(orderEntity, code);
            }

//            for (OrderDishes orderDishes : orderEntity.getOrderDetails().getOrderDishes()) {
//                Dish dishEntity = dishService.findById(orderDishes.getDish().getDishId());
//                dishes.add(dishEntity);
//            }

            orderEntity.setOrderStatus(OrderStatus.PENDING);
            OrderEntity order = orderRepository.save(orderEntity);
            //update code to make it used
            accessCodeService.updateAccessCodeToUsed(code);
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
                            "Error while creating order , please inform the waiter"));
        }
    }


    public OrderEntityDto getOrderById(Long id) {

        return orderRepository.findById(id)
                .map(OrderMapper::toDto)
                .orElse(null);
    }

    public List<OrderEntityDto> getAllOrders() {
        return null;
    }


    public void validateOrder(Long orderId) {
        try {
            orderRepository.validateOrder(orderId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while validating order");
        }
    }

    private void enrichOrderEntityWithCode(OrderEntity orderEntity, String code) {
        AccessCode accessCode = accessCodeService.getAccessCodeEntityWhenCodeIsValide(code);
        String[] codeParts = code.split("-");
        int tableNumber = Integer.parseInt(codeParts[1]);
        orderEntity.setWaiter(accessCode.getEmployee());
        orderEntity.setTableNumber(tableNumber);
    }


}
