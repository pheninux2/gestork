package pheninux.xdev.gestork.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pheninux.xdev.gestork.security.service.JwtService;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String SECRET_KEY = "zDgIc0P7ORSqAp350mFZO1b9E7HUUBDeGWXNwM7f6fc";

    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public JwtRequestFilter(@Lazy AuthenticationProvider authenticationProvider, JwtService jwtService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/**")) {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is required");
                return;
            }
            String username = "";
            String jwt = "";

            // Vérifiez si le header de l'autorisation est présent et commence par "Bearer "
            if (authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7); // Enlevez "Bearer "
                try {
                    username = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(jwt)
                            .getBody()
                            .getSubject();
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT expired");
                } catch (Exception e) {
                    System.out.println("JWT invalid");
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtService.validateToken(jwt, username)) {
                    Authentication authRequest = new UsernamePasswordAuthenticationToken(username, null);
                    Authentication authResult = authenticationProvider.authenticate(authRequest);
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                } else {
                    logger.error("Authentication failed for " + username + ", invalidate token");
                    throw new BadCredentialsException("Token invalide");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
