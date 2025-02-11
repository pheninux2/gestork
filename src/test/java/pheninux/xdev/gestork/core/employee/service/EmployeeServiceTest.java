package pheninux.xdev.gestork.core.employee.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindEmployeeById() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeById(1L);

        assertNotNull(foundEmployee);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Employee foundEmployee = employeeService.findEmployeeById(1L);

        assertNull(foundEmployee);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeesByRole() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        when(employeeRepository.findEmployeesByRole(EmployeeRole.WAITER, Sort.by("name"))).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name"));

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findEmployeesByRole(EmployeeRole.WAITER, Sort.by("name"));
    }
}