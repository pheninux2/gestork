package pheninux.xdev.gestork.core.order.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.core.order.model.OrderEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Modifying
    @Transactional
    @Query("update OrderEntity o set o.orderStatus = 'VALIDATE' where o.orderId = :orderId")
    void validateOrder(Long orderId);


    List<OrderEntity> findOrderEntityByTableNumber(Integer tableNumber);
}
