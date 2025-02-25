package pheninux.xdev.gestork.web.order.fragment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;
import pheninux.xdev.gestork.core.order.service.OrderService;

@Controller
@RequestMapping("/fragment/order")
public class OrderFragmentController {

    private final OrderService orderService;

    public OrderFragmentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/dishesSummary/{orderId}")
    public String dishesSummary(Model model, @PathVariable Long orderId) {
        OrderEntityDto order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/fragment/dishesSummary";
    }
}
