//package pheninux.xdev.gestork.web;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.ui.Model;
//
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//class HomeDispatcherControllerTest {
//
//    @InjectMocks
//    private HomeDispatcherController homeDispatcherController;
//
//    @Mock
//    private Model model;
//
//    @Mock
//    private SecurityContext securityContext;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        SecurityContextHolder.setContext(securityContext);
//    }
//
//    @Test
//    void testEmployeeHomePage_Waiter() {
//        Authentication authentication = new TestingAuthenticationToken(
//                "adil", // principal
//                null, // credentials
//                "ROLE_WAITER"); // authority roles
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(authentication.getName()).thenReturn((String) authentication.getPrincipal());
//        when(authentication.getAuthorities()).thenReturn((Collection) authentication.getAuthorities());
//
//
//        String viewName = homeDispatcherController.employeeHomePage(model);
//
//        assertEquals("employee/waiter/layout/home", viewName);
//    }
//
//    @Test
//    void testEmployeeHomePage_Admin() {
//        Authentication authentication = new TestingAuthenticationToken(
//                "adil", // principal
//                null, // credentials
//                "ROLE_ADMIN"); // authority roles
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(authentication.getName()).thenReturn((String) authentication.getPrincipal());
//        when(authentication.getAuthorities()).thenReturn((Collection) authentication.getAuthorities());
//
//        String viewName = homeDispatcherController.employeeHomePage(model);
//
//        assertEquals("employee/admin/layout/home", viewName);
//    }
//
//    @Test
//    void testEmployeeHomePage_Chef() {
//        Authentication authentication = new TestingAuthenticationToken(
//                "adil", // principal
//                null, // credentials
//                "ROLE_CHEF"); // authority roles
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(authentication.getName()).thenReturn((String) authentication.getPrincipal());
//        when(authentication.getAuthorities()).thenReturn((Collection) authentication.getAuthorities());
//
//        String viewName = homeDispatcherController.employeeHomePage(model);
//
//        assertEquals("employee/chef/layout/home", viewName);
//    }
//
//    @Test
//    void testEmployeeHomePage_NotAuthenticated() {
//        Authentication authentication = new TestingAuthenticationToken(
//                "adil", // principal
//                null, // credentials
//                "ROLE_CHEF"); // authority roles
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(false);
//
//        String viewName = homeDispatcherController.employeeHomePage(model);
//
//        assertEquals("employee/login", viewName);
//    }
//
//    @Test
//    void testCustomerHomePage() {
//        Authentication authentication = new TestingAuthenticationToken(
//                "adil", // principal
//                null, // credentials
//                "ROLE_CHEF"); // authority roles
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("customerUser");
//
//        String viewName = homeDispatcherController.customerHomePage(model);
//
//        assertEquals("customer/layout/home", viewName);
//    }
//}