//package pheninux.xdev.gestork.security.filter;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import pheninux.xdev.gestork.security.service.JwtService;
//
//import java.io.IOException;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//class JwtRequestFilterTest {
//
//    @InjectMocks
//    private JwtRequestFilter jwtRequestFilter;
//
//    @Mock
//    private AuthenticationProvider authenticationProvider;
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
//    private FilterChain filterChain;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testDoFilterInternal_MissingToken() throws ServletException, IOException {
//        when(request.getRequestURI()).thenReturn("/api/test");
//        when(request.getHeader("Authorization")).thenReturn(null);
//
//        jwtRequestFilter.doFilterInternal(request, response, filterChain);
//
//        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is required");
//        verify(filterChain, never()).doFilter(request, response);
//    }
//
//    @Test
//    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
//        when(request.getRequestURI()).thenReturn("/api/test");
//        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
//
//        jwtRequestFilter.doFilterInternal(request, response, filterChain);
//
//        verify(response, never()).sendError(anyInt(), anyString());
//        verify(filterChain).doFilter(request, response);
//    }
//
//    @Test
//    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
//        String username = "testUser";
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                .signWith(SignatureAlgorithm.HS512, JwtRequestFilter.SECRET_KEY)
//                .compact();
//
//        when(request.getRequestURI()).thenReturn("/api/test");
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtService.validateToken(token, username)).thenReturn(true);
//        when(authenticationProvider.authenticate(any(Authentication.class)))
//                .thenReturn(new UsernamePasswordAuthenticationToken(username, null, null));
//
//        jwtRequestFilter.doFilterInternal(request, response, filterChain);
//
//        verify(filterChain).doFilter(request, response);
//        verify(response, never()).sendError(anyInt(), anyString());
//        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
//    }
//}