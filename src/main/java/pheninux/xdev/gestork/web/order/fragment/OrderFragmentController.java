package pheninux.xdev.gestork.web.order.fragment;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;
import pheninux.xdev.gestork.core.order.service.OrderService;

import java.util.List;

import static pheninux.xdev.gestork.utils.Utils.getTableNumber;

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

    @GetMapping("/status/{code}")
    public String orderStatus(@PathVariable String code, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        int tableNumber = getTableNumber(code);
        List<OrderEntityDto> orders = orderService.getOrdersByTableNumber(tableNumber);
        model.addAttribute("orders", orders);
        model.addAttribute("login", login);
        return "order/fragment/orderStatusFragment";
    }
}
