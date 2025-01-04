package pheninux.xdev.gestork.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pheninux.xdev.gestork.service.CustomEmployeeDetailsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomEmployeeDetailsService customEmployeeDetailsService;
    private static final int MAX_ATTEMPTS = 5;
    private Map<String, Integer> attemptsCache = new HashMap<>();

    public CustomAuthenticationFilter(CustomEmployeeDetailsService customEmployeeDetailsService) {
        super();
        this.customEmployeeDetailsService = customEmployeeDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = obtainUsername(request);
        attemptsCache.putIfAbsent(username, 0);

        logAttempt(username);

        if (attemptsCache.get(username) >= MAX_ATTEMPTS) {
            logLockout(username);
            throw new LockedException("User account is locked due to too many failed attempts.");
        }

        UserDetails userDetails = customEmployeeDetailsService.loadUserByUsername(username);

        logUserDetails(userDetails);

        if (userDetails == null) {
            attemptsCache.put(username, attemptsCache.get(username) + 1);
            return null; // Retourner null si l'utilisateur n'est pas trouvé
        }

        // Appeler la méthode parente pour tenter l'authentification
        return super.attemptAuthentication(request, response);
    }

    private void logAttempt(String username) {
        logger.debug("Auth attempt for user: " + username);
    }

    private void logLockout(String username) {
        logger.warn("User account locked due to max attempts reached: " + username);
    }

    private void logUserDetails(UserDetails userDetails) {
        if (userDetails != null) {
            logger.debug("Loaded user details: " + userDetails.getUsername());
            logger.debug("Roles: " + userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", ")));
        } else {
            logger.debug("No user details found");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        super.successfulAuthentication(request, response, chain, authResult);
        logger.info("Authentication successful for user: " + authResult.getName());
    }
}
