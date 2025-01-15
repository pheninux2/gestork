package pheninux.xdev.gestork.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/employee/home")
    public String employeeHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            String role = authentication.getAuthorities().stream()
                    .filter(authority -> authority.getAuthority() != null)
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            switch (role) {
                case "ROLE_SERVER":
                    return "employee/waiter/home";
                case "ROLE_ADMIN":
                    return "employee/admin/home";
                case "ROLE_CHEF":
                    return "employee/chef/home";
            }
        }
        return "employee/login";
    }

    @GetMapping("/customer/home")
    public String customerHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        model.addAttribute("customerLogin", login);
        return "customer/home";
    }

}
