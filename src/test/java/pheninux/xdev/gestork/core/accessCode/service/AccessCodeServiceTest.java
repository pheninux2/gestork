package pheninux.xdev.gestork.core.accessCode.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;
import pheninux.xdev.gestork.core.accessCode.repository.AccessCodeRepository;
import pheninux.xdev.gestork.core.customer.model.Customer;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;
import pheninux.xdev.gestork.utils.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccessCodeServiceTest {

    @InjectMocks
    private AccessCodeService accessCodeService;

    @Mock
    private AccessCodeRepository accessCodeRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAndSaveAccessCode() {
        Customer customer = new Customer();
        customer.setLogin("customer1");
        int tableNumber = 1;

        Employee waiter = new Employee();
        waiter.setLogin("waiter1");

        when(employeeRepository.findEmployeeByLogin(anyString())).thenReturn(waiter);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getLogin).thenReturn("waiter1");

            String code = accessCodeService.generateAndSaveAccessCode(customer, tableNumber);

            assertNotNull(code);
            verify(accessCodeRepository, times(1)).saveAndFlush(any(AccessCode.class));
        }
    }

    @Test
    void testGenerateAccessCode() {
        int tableNumber = 1;
        String code = accessCodeService.generateAccessCode(tableNumber);

        assertNotNull(code);
        assertTrue(code.matches("[a-zA-Z0-9]{6}-1"));
    }

    @Test
    void testIsAccessCodeValid_ValidCode() {
        String code = "validCode-1";
        AccessCode accessCode = new AccessCode();
        accessCode.setCode(code);
        accessCode.setUsed(false);
        accessCode.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));

        when(accessCodeRepository.findByCode(code)).thenReturn(accessCode);

        boolean isValid = accessCodeService.isAccessCodeValid(code);

        assertTrue(isValid);
    }

    @Test
    void testIsAccessCodeValid_InvalidCode() {
        String code = "invalidCode-1";
        AccessCode accessCode = new AccessCode();
        accessCode.setCode(code);
        accessCode.setUsed(true);
        accessCode.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));

        when(accessCodeRepository.findByCode(code)).thenReturn(accessCode);

        boolean isValid = accessCodeService.isAccessCodeValid(code);

        assertFalse(isValid);
    }
}