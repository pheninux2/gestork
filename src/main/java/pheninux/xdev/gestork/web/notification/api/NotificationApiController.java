package pheninux.xdev.gestork.web.notification.api;

import org.springframework.web.bind.annotation.*;
import pheninux.xdev.gestork.core.notification.model.NotificationDto;
import pheninux.xdev.gestork.core.notification.service.NotificationService;
import pheninux.xdev.gestork.response.CustomResponseBody;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationApiController {

    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/get/{waiterLogin}")
    public CustomResponseBody<List<NotificationDto>> getNotificationsByWaiterId(@PathVariable String waiterLogin) {
        return notificationService.fetchUnreadNotificationsForWaiter(waiterLogin);
    }

    @GetMapping("/all")
    public CustomResponseBody<List<NotificationDto>> getAllNotifications() {
        return notificationService.fetchAllNotifications();
    }

    @PutMapping("/markAsRead/{notificationId}")
    public CustomResponseBody<NotificationDto> markNotificationAsRead(@PathVariable Long notificationId) {
        return notificationService.markNotificationAsRead(notificationId);
    }

}
