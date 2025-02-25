package pheninux.xdev.gestork.core.table.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pheninux.xdev.gestork.core.accessCode.service.AccessCodeService;
import pheninux.xdev.gestork.core.customer.model.Customer;
import pheninux.xdev.gestork.core.customer.model.CustomerRole;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;
import pheninux.xdev.gestork.core.table.model.CustomerTable;
import pheninux.xdev.gestork.core.table.model.TableStatus;
import pheninux.xdev.gestork.core.table.repository.TableRepository;
import pheninux.xdev.gestork.utils.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pheninux.xdev.gestork.utils.Utils.renderAlertSingle;

class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccessCodeService accessCodeService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCode_TableNotExist() {
        String clientLogin = "testClient";
        int tableNumber = 1;

        when(tableRepository.findTableByNumber(tableNumber)).thenReturn(null);

        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.renderAlertSingle("alert-danger", "La table n'existe pas.")).thenReturn("La table n'existe pas.");
            ResponseEntity<String> response = tableService.generateCode(clientLogin, tableNumber);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(renderAlertSingle("alert-danger", "La table n'existe pas."), response.getBody());
        }

    }

    @Test
    void testGenerateCode_TableAlreadyOccupied() {
        String clientLogin = "testClient";
        int tableNumber = 1;
        CustomerTable customerTable = new CustomerTable();
        customerTable.setStatut(TableStatus.BUSY);

        when(tableRepository.findTableByNumber(tableNumber)).thenReturn(customerTable);

        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.renderAlertSingle("alert-danger", "La table est déjà occupée.")).thenReturn("La table est déjà occupée.");
            ResponseEntity<String> response = tableService.generateCode(clientLogin, tableNumber);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(renderAlertSingle("alert-danger", "La table est déjà occupée."), response.getBody());
        }

    }

    @Test
    void testGenerateCode_CustomerExists() {
        String clientLogin = "testClient";
        int tableNumber = 1;
        CustomerTable customerTable = new CustomerTable();
        customerTable.setStatut(TableStatus.AVAILABLE);
        Customer customer = new Customer();
        String code = "123456";

        when(tableRepository.findTableByNumber(tableNumber)).thenReturn(customerTable);
        when(customerRepository.findClientByLogin(clientLogin)).thenReturn(customer);
        when(accessCodeService.generateAndSaveAccessCode(customer, tableNumber)).thenReturn(code);

        ResponseEntity<String> response = tableService.generateCode(clientLogin, tableNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("<script>window.location.href='/view/code/display?code=" + code + "';</script>", response.getBody());
    }

    @Test
    void testGenerateCode_CustomerNotExists() {
        String clientLogin = "testClient";
        int tableNumber = 1;
        CustomerTable customerTable = new CustomerTable();
        customerTable.setStatut(TableStatus.AVAILABLE);
        Customer newCustomer = new Customer();
        newCustomer.setLogin(clientLogin);
        newCustomer.setRole(CustomerRole.CUSTOMER);
        String code = "123456";

        when(tableRepository.findTableByNumber(tableNumber)).thenReturn(customerTable);
        when(customerRepository.findClientByLogin(clientLogin)).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(accessCodeService.generateAndSaveAccessCode(any(Customer.class), eq(tableNumber))).thenReturn(code);

        ResponseEntity<String> response = tableService.generateCode(clientLogin, tableNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("<script>window.location.href='/view/code/display?code=" + code + "';</script>", response.getBody());
        verify(customerRepository, times(1)).saveAndFlush(any(Customer.class));
    }
}