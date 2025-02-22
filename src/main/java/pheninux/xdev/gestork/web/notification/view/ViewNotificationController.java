package pheninux.xdev.gestork.web.notification.view;

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
@RequestMapping("/view/notification")
public class ViewNotificationController {

    private final NotificationService notificationService;

    public ViewNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/modal")
    public String getOrdersModal(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        CustomResponseBody<List<NotificationDto>> ordersNotification = notificationService.fetchUnreadNotificationsForWaiter(login);

        model.addAttribute("ordersNotification", ordersNotification);
        return "notification/fragment/modal"; // Chemin vers le fichier de template contenant le HTML de la modal
    }
}
