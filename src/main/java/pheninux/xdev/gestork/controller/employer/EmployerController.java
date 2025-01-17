package pheninux.xdev.gestork.controller.employer;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employee")
public class EmployerController {

    @GetMapping("/login")
    public String login() {
        return "employee/login";
    }

    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    @GetMapping("/generateCode-page")
    public String displayGenerateCodePage() {
        return "employee/waiter/services/generate-code";
    }

    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    @GetMapping("/showAccessCode-page")
    public String displayAccessCodePage(@RequestParam("code") String code, Model model) {
        model.addAttribute("accessCode", code);
        return "employee/waiter/show-access-code";
    }

    @GetMapping("/admin/addDish-page")
    public String displayAddDishPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Vous n'avez pas la permission d'accéder à cette page.");
        }

        return "employee/admin/addDish";
    }

}
