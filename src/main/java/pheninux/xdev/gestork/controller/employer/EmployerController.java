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
import pheninux.xdev.gestork.exception.CustomServiceException;
import pheninux.xdev.gestork.model.Dish;
import pheninux.xdev.gestork.service.DishService;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployerController {

    private final DishService dishService;

    public EmployerController(DishService dishService) {
        this.dishService = dishService;
    }

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
        isAdmin();
        return "employee/admin/addDish";
    }

    @GetMapping("/admin/manageSpecialPrices")
    public String manageSpecialPrices() {
        isAdmin();
        return "employee/admin/specialPrices";
    }

    @GetMapping("/admin/getDishes")
    public String getDishes(Model model) throws CustomServiceException {
        isAdmin();
        List<Dish> dishes = dishService.getDishes();
        model.addAttribute("dishes", dishes);
        return "employee/admin/dishesFragment";
    }

    private static void isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Vous n'avez pas la permission d'accéder à cette page.");
        }
    }

}
