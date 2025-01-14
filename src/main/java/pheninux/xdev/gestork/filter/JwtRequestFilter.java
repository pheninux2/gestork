package pheninux.xdev.gestork.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String SECRET_KEY = "zDgIc0P7ORSqAp350mFZO1b9E7HUUBDeGWXNwM7f6fc";

    private final AuthenticationProvider authenticationProvider;

    public JwtRequestFilter(@Lazy AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api")) {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is required");
                return;
            }
            String username = "";
            String jwt;

            // Vérifiez si le header de l'autorisation est présent et commence par "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
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
            // Si l'utilisateur n'est pas déjà authentifié, le définir dans le contexte de sécurité
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Authentication authRequest = new UsernamePasswordAuthenticationToken(username, null);

                Authentication authResult = authenticationProvider.authenticate(authRequest);
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
        }

        filterChain.doFilter(request, response);
    }
}
