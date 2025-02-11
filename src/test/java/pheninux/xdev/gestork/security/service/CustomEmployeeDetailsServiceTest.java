package pheninux.xdev.gestork.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomEmployeeDetailsServiceTest {

    @InjectMocks
    private CustomEmployeeDetailsService customEmployeeDetailsService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        Employee employee = new Employee();
        employee.setLogin("testEmployee");
        employee.setPassword("password");
        employee.setRole(EmployeeRole.ADMIN);

        when(employeeRepository.findEmployeeByLogin("testEmployee")).thenReturn(employee);

        var userDetails = customEmployeeDetailsService.loadUserByUsername("testEmployee");

        assertNotNull(userDetails);
        assertEquals("testEmployee", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(employeeRepository.findEmployeeByLogin("testEmployee")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> customEmployeeDetailsService.loadUserByUsername("testEmployee"));
    }
}