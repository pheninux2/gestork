//package pheninux.xdev.gestork.handler;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import pheninux.xdev.gestork.security.service.JwtService;
//
//import java.io.PrintWriter;
//import java.util.Collections;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class JwtAuthenticationSuccessHandlerTest {
//
//    @InjectMocks
//    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private Authentication authentication;
//
//    @Mock
//    private PrintWriter writer;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testOnAuthenticationSuccess_Customer() throws Exception {
//        String username = "customer";
//        String role = "[ROLE_CUSTOMER]";
//        String token = "jwt-token";
//
//        when(authentication.getName()).thenReturn(username);
//        when(authentication.getAuthorities()).thenReturn(Collections.singletonList((GrantedAuthority) () -> "ROLE_CUSTOMER"));
//        when(jwtService.generateToken(username, role)).thenReturn(token);
//        when(response.getWriter()).thenReturn(writer);
//
//        jwtAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//
//        verify(response).setHeader("Authorization", "Bearer " + token);
//        verify(writer).write("<script>localStorage.setItem('jwt', '" + token + "'); window.location.href='/customer/home';</script>");
//        verify(writer).flush();
//    }
//
//    @Test
//    void testOnAuthenticationSuccess_Employee() throws Exception {
//        String username = "employee";
//        String role = "[ROLE_EMPLOYEE]";
//        String token = "jwt-token";
//
//        when(authentication.getName()).thenReturn(username);
//        when(authentication.getAuthorities()).thenReturn(Collections.singletonList((GrantedAuthority) () -> "ROLE_EMPLOYEE"));
//        when(jwtService.generateToken(username, role)).thenReturn(token);
//        when(response.getWriter()).thenReturn(writer);
//
//        jwtAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//
//        verify(response).setHeader("Authorization", "Bearer " + token);
//        verify(writer).write("<script>localStorage.setItem('jwt', '" + token + "'); window.location.href='/employee/home';</script>");
//        verify(writer).flush();
//    }
//}