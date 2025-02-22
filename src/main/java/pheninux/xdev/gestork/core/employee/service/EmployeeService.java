package pheninux.xdev.gestork.core.employee.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee findEmployeeByLogin(String login) {
        return employeeRepository.findEmployeeByLogin(login);
    }

    public List<Employee> getEmployeesByRole(EmployeeRole role, Sort sort) {
        return employeeRepository.findEmployeesByRole(role, sort);
    }

}
