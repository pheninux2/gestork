package pheninux.xdev.gestork.web.notification.fragment;

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
@RequestMapping("/fragment/notification")
public class NotificationFragmentController {

    private final NotificationService notificationService;

    public NotificationFragmentController(NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        CustomResponseBody<List<NotificationDto>> notifications =
                notificationService.fetchUnreadNotificationsForWaiter(login);
        model.addAttribute("ordersNotification", notifications);
        return "notification/fragment/notificationOrder";
    }
}
