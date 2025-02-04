package pheninux.xdev.gestork.core.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.core.table.model.EmployeeTableAssignment;

@Repository
public interface EmployeeTableAssignmentRepository extends JpaRepository<EmployeeTableAssignment, Long> {

}
