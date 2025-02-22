package pheninux.xdev.gestork.core.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.service.EmployeeService;
import pheninux.xdev.gestork.core.notification.mapper.NotificationMapper;
import pheninux.xdev.gestork.core.notification.model.Notification;
import pheninux.xdev.gestork.core.notification.model.NotificationDto;
import pheninux.xdev.gestork.core.notification.repository.NotificationRepository;
import pheninux.xdev.gestork.response.CustomResponseBody;
import pheninux.xdev.gestork.utils.Utils;

import java.util.List;

@Service
public class NotificationService {

    private static final String NOTIFICATION_CREATED = "Notification created successfully";
    private static final String NOTIFICATION_NOT_FOUND = "Notification not found";
    private static final String NOTIFICATION_DELETED = "Notification deleted successfully";
    private static final String NOTIFICATION_UPDATED = "Notification updated successfully";
    private static final String ERROR_ADDING_NOTIFICATION = "Error while adding notification";
    private static final String NOTIFICATION_FOUND = "Notification found";
    private static final String ERROR_FETCHING_NOTIFICATIONS = "Error while fetching notifications";
    private static final String ERROR_UPDATING_NOTIFICATIONS = "Error while updating notifications";
    private static final String NOTIFICATION_NOT_UPDATED = "Notification not updated";

    private final static Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final EmployeeService employeeService;

    public NotificationService(NotificationRepository notificationRepository, EmployeeService employeeService) {
        this.notificationRepository = notificationRepository;
        this.employeeService = employeeService;
    }

    public CustomResponseBody<NotificationDto> createNotification(Notification notification) {
        try {
            Notification savedNotification = notificationRepository.save(notification);
            logger.info(NOTIFICATION_CREATED);
            return new CustomResponseBody<>(NotificationMapper.toDto(savedNotification),
                    NOTIFICATION_CREATED,
                    200,
                    Utils.renderAlertSingle("alert-success", NOTIFICATION_CREATED));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new CustomResponseBody<>(null,
                    ERROR_ADDING_NOTIFICATION,
                    500,
                    Utils.renderAlertSingle("alert-danger", ERROR_ADDING_NOTIFICATION));
        }
    }

    public CustomResponseBody<List<NotificationDto>> fetchUnreadNotificationsForWaiter(String waiterLogin) {
        try {
            Employee waiter = employeeService.findEmployeeByLogin(waiterLogin);
            List<Notification> notification = notificationRepository.findAllByEmployeeId_EmployeeIdAndHasBeenReadFalse(waiter.getEmployeeId());
            if (notification.isEmpty()) {
                return new CustomResponseBody<>(null,
                        NOTIFICATION_NOT_FOUND,
                        404,
                        Utils.renderAlertSingle("alert-danger",
                                NOTIFICATION_NOT_FOUND));
            }
            return new CustomResponseBody<>(NotificationMapper.toDtoList(notification),
                    NOTIFICATION_FOUND,
                    200,
                    Utils.renderAlertSingle("alert-success",
                            NOTIFICATION_FOUND));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new CustomResponseBody<>(null,
                    ERROR_FETCHING_NOTIFICATIONS,
                    500,
                    Utils.renderAlertSingle("alert-danger",
                            ERROR_FETCHING_NOTIFICATIONS));
        }
    }


    public CustomResponseBody<List<NotificationDto>> fetchAllNotifications() {
        try {
            List<Notification> notifications = notificationRepository.findAll();
            return new CustomResponseBody<>(NotificationMapper.toDtoList(notifications),
                    NOTIFICATION_FOUND,
                    200,
                    Utils.renderAlertSingle("alert-success",
                            NOTIFICATION_FOUND));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new CustomResponseBody<>(null,
                    ERROR_FETCHING_NOTIFICATIONS,
                    500,
                    Utils.renderAlertSingle("alert-danger",
                            ERROR_FETCHING_NOTIFICATIONS));
        }
    }

    public CustomResponseBody<NotificationDto> markNotificationAsRead(Long notificationId) {
        try {
            notificationRepository.updateNotificationHasBeenReadTrue(notificationId);
            return new CustomResponseBody<>(null,
                    NOTIFICATION_UPDATED,
                    200,
                    Utils.renderAlertSingle("alert-success",
                            NOTIFICATION_UPDATED));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new CustomResponseBody<>(null,
                    ERROR_UPDATING_NOTIFICATIONS,
                    500,
                    Utils.renderAlertSingle("alert-danger",
                            ERROR_FETCHING_NOTIFICATIONS));
        }
    }
}
