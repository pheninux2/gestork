package pheninux.xdev.gestork.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pheninux.xdev.gestork.service.CustomClientDetailsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomCustomerAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomClientDetailsService customClientDetailsService;
    private static final int MAX_ATTEMPTS = 5;
    private final Map<String, Integer> attemptsCache = new HashMap<>();

    public CustomCustomerAuthenticationFilter(CustomClientDetailsService customClientDetailsService) {
        this.customClientDetailsService = customClientDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = obtainUsername(request);
        attemptsCache.putIfAbsent(username, 0);

        logAttempt(username);

        // Vérifiez si l'utilisateur est bloqué
        if (attemptsCache.get(username) >= MAX_ATTEMPTS) {
            logLockout(username);
            throw new LockedException("User account is locked due to too many failed attempts.");
        }

        // Chargement des détails de l'utilisateur
        UserDetails userDetails = customClientDetailsService.loadUserByUsername(username);

        // Vérifiez si l'utilisateur existe
        if (userDetails == null) {
            attemptsCache.put(username, attemptsCache.get(username) + 1);
            logFailedAttempt(username);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Appeler la méthode parente pour tenter l'authentification
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, obtainPassword(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void logAttempt(String username) {
        logger.debug("Auth attempt for user: " + username);
    }

    private void logLockout(String username) {
        logger.warn("User account locked due to max attempts reached: " + username);
    }

    private void logFailedAttempt(String username) {
        logger.warn("Failed authentication attempt for user: " + username);
        attemptsCache.put(username, attemptsCache.get(username) + 1);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        logger.warn("Authentication failed for user: " + obtainUsername(request));
    }
}
