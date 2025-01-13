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
                    return "employee/waiter/waiter-home";
                case "ROLE_ADMIN":
                    return "employee/admin/admin-home";
                case "ROLE_CHEF":
                    return "employee/chef/chef-home";
            }
        }
        return "employee/login";
    }

    @GetMapping("/customer/home")
    public String customerHomePage() {
        return "customer/home";
    }

}
