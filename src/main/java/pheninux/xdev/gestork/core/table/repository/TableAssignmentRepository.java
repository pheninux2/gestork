package pheninux.xdev.gestork.core.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.core.table.model.TableAssignment;

import java.util.List;

@Repository
public interface TableAssignmentRepository extends JpaRepository<TableAssignment, Long> {

    @Query("SELECT ta FROM TableAssignment ta WHERE ta.employee.employeeId = ?1")
    List<TableAssignment> findTableAssignmentsByEmployeeId(Long employeeId);

    TableAssignment findTableAssignmentByTableNumber(int tableNumber);
}
