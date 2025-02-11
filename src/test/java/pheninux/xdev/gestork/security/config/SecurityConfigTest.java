//package pheninux.xdev.gestork.security.config;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import pheninux.xdev.gestork.handler.JwtAuthenticationSuccessHandler;
//import pheninux.xdev.gestork.security.filter.JwtRequestFilter;
//import pheninux.xdev.gestork.security.service.CustomClientDetailsService;
//import pheninux.xdev.gestork.security.service.CustomEmployeeDetailsService;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//class SecurityConfigTest {
//
//    @InjectMocks
//    private SecurityConfig securityConfig;
//
//    @Mock
//    private CustomClientDetailsService customClientDetailsService;
//
//    @Mock
//    private CustomEmployeeDetailsService customEmployeeDetailsService;
//
//    @Mock
//    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
//
//    @Mock
//    private JwtRequestFilter jwtRequestFilter;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testPasswordEncoder() {
//        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
//        assertNotNull(passwordEncoder);
//    }
//
//    @Test
//    void testCustomerSecurityFilterChain() throws Exception {
//        HttpSecurity httpSecurity = mock(HttpSecurity.class);
//        SecurityFilterChain filterChain = securityConfig.CustomerSecurityFilterChain(httpSecurity);
//        assertNotNull(filterChain);
//    }
//
//    @Test
//    void testEmployeeSecurityFilterChain() throws Exception {
//        SecurityFilterChain filterChain = securityConfig.EmployeeSecurityFilterChain(null);
//        assertNotNull(filterChain);
//    }
//
//    @Test
//    void testH2SecurityFilterChain() throws Exception {
//        SecurityFilterChain filterChain = securityConfig.H2SecurityFilterChain(null);
//        assertNotNull(filterChain);
//    }
//
//    @Test
//    void testStaticSecurityFilterChain() throws Exception {
//        SecurityFilterChain filterChain = securityConfig.StaticSecurityFilterChain(null);
//        assertNotNull(filterChain);
//    }
//
//    @Test
//    void testAuthManager() throws Exception {
//        AuthenticationManager authManager = securityConfig.authManager(null);
//        assertNotNull(authManager);
//    }
//
//    @Test
//    void testCustomerAuthenticationProvider() throws Exception {
//        assertNotNull(securityConfig.CustomerAuthenticationProvider(null));
//    }
//}