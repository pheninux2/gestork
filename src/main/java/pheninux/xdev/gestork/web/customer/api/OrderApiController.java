package pheninux.xdev.gestork.web.customer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pheninux.xdev.gestork.core.order.model.OrderEntity;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;
import pheninux.xdev.gestork.core.order.service.OrderService;
import pheninux.xdev.gestork.response.CustomResponseBody;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/order")
public class OrderApiController {

    private final OrderService orderService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomResponseBody<OrderEntityDto> createdOrders = orderService.createOrder(order);

        if (isOrderCreated(createdOrders)) {
            notifyWaiters();
        }

        return createdOrders;
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        try {
            emitter.send(SseEmitter.event().data("Connected to stream"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    private boolean isOrderCreated(CustomResponseBody<OrderEntityDto> response) {
        return response.getStatus() == 200;
    }

    private void notifyWaiters() {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data("New order created"));
            } catch (Exception e) {
                emitter.completeWithError(e);

            }
        });
    }
}


