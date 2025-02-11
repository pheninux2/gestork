package pheninux.xdev.gestork.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;
import pheninux.xdev.gestork.core.accessCode.repository.AccessCodeRepository;
import pheninux.xdev.gestork.core.customer.model.Customer;
import pheninux.xdev.gestork.core.customer.model.CustomerRole;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomClientDetailsServiceTest {

    @InjectMocks
    private CustomClientDetailsService customClientDetailsService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccessCodeRepository accessCodeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        Customer customer = new Customer();
        customer.setLogin("testUser");
        customer.setPassword("password");
        customer.setRole(CustomerRole.CUSTOMER);

        when(customerRepository.findClientByLogin("testUser")).thenReturn(customer);

        var userDetails = customClientDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(customerRepository.findClientByLogin("testUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> customClientDetailsService.loadUserByUsername("testUser"));
    }

    @Test
    void testLoadUserByUsernameWithCode_ValidCode() {
        Customer customer = new Customer();
        customer.setLogin("testUser");
        customer.setPassword("password");
        customer.setRole(CustomerRole.CUSTOMER);

        AccessCode accessCode = new AccessCode();
        accessCode.setCode("validCode");
        accessCode.setUsed(false);
        accessCode.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));

        when(customerRepository.findClientByLogin("testUser")).thenReturn(customer);
        when(accessCodeRepository.findByCode("validCode")).thenReturn(accessCode);

        var userDetails = customClientDetailsService.loadUserByUsername("testUser", "validCode");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(accessCode.isUsed());
    }

    @Test
    void testLoadUserByUsernameWithCode_InvalidCode() {
        when(customerRepository.findClientByLogin("testUser")).thenReturn(null);
        when(accessCodeRepository.findByCode("invalidCode")).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> customClientDetailsService.loadUserByUsername("testUser", "invalidCode"));
    }

    @Test
    void testLoadUserByUsernameWithCode_ExpiredCode() {
        Customer customer = new Customer();
        customer.setLogin("testUser");
        customer.setPassword("password");
        customer.setRole(CustomerRole.CUSTOMER);

        AccessCode accessCode = new AccessCode();
        accessCode.setCode("expiredCode");
        accessCode.setUsed(false);
        accessCode.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));

        when(customerRepository.findClientByLogin("testUser")).thenReturn(customer);
        when(accessCodeRepository.findByCode("expiredCode")).thenReturn(accessCode);

        assertThrows(BadCredentialsException.class, () -> customClientDetailsService.loadUserByUsername("testUser", "expiredCode"));
    }
}