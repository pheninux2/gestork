//package pheninux.xdev.gestork.security.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import pheninux.xdev.gestork.security.filter.JwtRequestFilter;
//
//import java.util.Date;
//import java.util.HashMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JwtServiceTest {
//
//    @InjectMocks
//    private JwtService jwtService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGenerateToken() {
//        String username = "testUser";
//        String role = "ROLE_USER";
//        String token = jwtService.generateToken(username, role);
//
//        assertNotNull(token);
//        Claims claims = Jwts.parser().setSigningKey(JwtRequestFilter.SECRET_KEY).parseClaimsJws(token).getBody();
//        assertEquals(username, claims.get("username"));
//        assertEquals(role, claims.get("role"));
//    }
//
//    @Test
//    void testValidateToken_ValidToken() {
//        String username = "testUser";
//        String role = "ROLE_USER";
//        String token = jwtService.generateToken(username, role);
//
//        assertTrue(jwtService.validateToken(token, username));
//    }
//
//    @Test
//    void testValidateToken_InvalidToken() {
//        String username = "testUser";
//        String token = "invalidToken";
//
//        assertFalse(jwtService.validateToken(token, username));
//    }
//
//    @Test
//    void testExtractUsername() {
//        String username = "testUser";
//        String role = "ROLE_USER";
//        String token = jwtService.generateToken(username, role);
//
//        String extractedUsername = jwtService.extractUsername(token);
//        assertEquals(username, extractedUsername);
//    }
//
//    @Test
//    void testIsTokenExpired() {
//        String username = "testUser";
//        String role = "ROLE_USER";
//        String token = Jwts.builder()
//                .setClaims(new HashMap<>())
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 11)) // 11 hours ago
//                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hour ago
//                .signWith(SignatureAlgorithm.HS256, JwtRequestFilter.SECRET_KEY)
//                .compact();
//
//        assertTrue(jwtService.isTokenExpired(token));
//    }
//}