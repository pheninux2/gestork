package pheninux.xdev.gestork.core.employee.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeeByLogin(String login);

    List<Employee> findEmployeesByRole(EmployeeRole employeeRole, Sort sort);
}
