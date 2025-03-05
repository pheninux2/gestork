package pheninux.xdev.gestork.core.table.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pheninux.xdev.gestork.core.table.model.CustomerTable;
import pheninux.xdev.gestork.core.table.model.TableAssignment;
import pheninux.xdev.gestork.core.table.model.TableStatus;

public interface TableRepository extends JpaRepository<CustomerTable, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE CustomerTable c SET c.statut = :status WHERE c.numeroTable = :tableNumber")
    void updateTableStatus(@Param("status") TableStatus status, @Param("tableNumber") int tableNumber);

    @Query("SELECT c FROM CustomerTable c WHERE c.numeroTable = :tableNumber")
    CustomerTable findTableByNumber(@Param("tableNumber") int tableNumber);

}

