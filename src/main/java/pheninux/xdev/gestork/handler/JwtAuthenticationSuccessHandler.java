package pheninux.xdev.gestork.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pheninux.xdev.gestork.service.JwtService;

import java.io.IOException;


@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationSuccessHandler.class);

    @Autowired
    private JwtService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        log.debug("authentication success");
        String username = authentication.getName();
        String role = authentication.getAuthorities().toString();
        String token = jwtService.generateToken(username, role);
        log.debug("token: {}", token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Définir le token dans l'en-tête de la réponse
        response.setHeader("Authorization", "Bearer " + token);

        // Renvoie une réponse HTML
        response.setContentType("text/html");
        if (role.equals("[ROLE_CLIENT]")) {
            response.getWriter().write("<script>localStorage.setItem('jwt', '" + token + "'); window.location.href='/customer/home';</script>");
        } else {
            response.getWriter().write("<script>localStorage.setItem('jwt', '" + token + "'); window.location.href='/employee/home';</script>");
        }
    }

}