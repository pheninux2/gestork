package pheninux.xdev.gestork.web.order.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.notification.model.NotificationDto;
import pheninux.xdev.gestork.core.notification.service.NotificationService;
import pheninux.xdev.gestork.response.CustomResponseBody;

import java.util.List;

@Controller
@RequestMapping("/view/order")
public class OrderViewController {

    private final NotificationService notificationService;

    public OrderViewController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/orderSummary")
    public String orderDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        CustomResponseBody<List<NotificationDto>> ordersNotification = notificationService.fetchUnreadNotificationsForWaiter(login);

        model.addAttribute("ordersNotification", ordersNotification);
        return "order/layout/orderSummary";
    }
}
