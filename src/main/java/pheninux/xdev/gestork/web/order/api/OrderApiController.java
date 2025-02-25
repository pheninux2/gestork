package pheninux.xdev.gestork.web.order.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pheninux.xdev.gestork.core.order.model.OrderEntity;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;
import pheninux.xdev.gestork.core.order.service.OrderService;
import pheninux.xdev.gestork.response.CustomResponseBody;
import pheninux.xdev.gestork.utils.Utils;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderEntityDto> getOrderById(@PathVariable Long id) {
        try {
            OrderEntityDto orderEntityDto = orderService.getOrderById(id);
            if (orderEntityDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(orderEntityDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<OrderEntityDto>> getAllOrders() {
        try {
            List<OrderEntityDto> orders = orderService.getAllOrders();
            if (orders == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public CustomResponseBody<OrderEntityDto> createOrder(@RequestBody OrderEntity order) {

        CustomResponseBody<OrderEntityDto> createdOrders = orderService.createOrder(order);

        if (isOrderCreated(createdOrders)) {
            Utils.notifyWaiters();
        }

        return createdOrders;
    }

    private boolean isOrderCreated(CustomResponseBody<OrderEntityDto> response) {
        return response.getStatus() == 200;
    }


}


