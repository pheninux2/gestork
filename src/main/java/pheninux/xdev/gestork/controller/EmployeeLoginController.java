package pheninux.xdev.gestork.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.repository.EmployeeRepository;

@Controller
@RequestMapping("/employee")
public class EmployeeLoginController {


    @GetMapping("/login")
    public String login() {
        return "employee/login";
    }

    @GetMapping("/home")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SERVER') or hasRole('CHEF')")
    public String redirectToHomePage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().stream()
                    .filter(authority -> authority.getAuthority() != null)
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            switch (role) {
                case "ROLE_SERVER":
                    return "employee/server-home";
                case "ROLE_ADMIN":
                    return "employee/admin-home";
                case "ROLE_CHEF":
                    return "employee/chef-home";
                default:
                    return "employee/default-home"; // ou une page d'erreur
            }
        }
        return "employee/login";
    }

}
