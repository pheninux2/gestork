package pheninux.xdev.gestork.core.notification.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.core.notification.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByEmployeeId_EmployeeIdAndHasBeenReadFalse(Long employeeId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.hasBeenRead = true WHERE n.id = :notificationId")
    void updateNotificationHasBeenReadTrue(Long notificationId);
}
