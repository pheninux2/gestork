package pheninux.xdev.gestork.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pheninux.xdev.gestork.core.notification.model.NotificationDto;
import pheninux.xdev.gestork.core.notification.service.NotificationService;
import pheninux.xdev.gestork.response.CustomResponseBody;

import java.util.List;

@Controller
public class HomeDispatcherController {

    private final NotificationService notificationService;

    public HomeDispatcherController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/employee/home")
    public String employeeHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            String role = authentication.getAuthorities().stream()
                    .filter(authority -> authority.getAuthority() != null)
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            switch (role) {
                case "ROLE_WAITER":
                    CustomResponseBody<List<NotificationDto>> notifications = notificationService.fetchUnreadNotificationsForWaiter(username);
                    model.addAttribute("orderNotificationsSize", notifications.getData());
                    return "employee/waiter/layout/home";
                case "ROLE_ADMIN":
                    return "employee/admin/layout/home";
                case "ROLE_CHEF":
                    return "employee/chef/layout/home";
            }
        }
        return "employee/login";
    }

    @GetMapping("/customer/home")
    public String customerHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        model.addAttribute("customerLogin", login);
        return "customer/layout/home";
    }

}
