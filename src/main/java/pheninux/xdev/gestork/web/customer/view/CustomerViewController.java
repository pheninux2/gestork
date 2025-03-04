package pheninux.xdev.gestork.web.customer.view;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.accessCode.service.AccessCodeService;

@Controller
@RequestMapping("/view/customer")
public class CustomerViewController {

    private final AccessCodeService accessCodeService;

    public CustomerViewController(AccessCodeService accessCodeService) {
        this.accessCodeService = accessCodeService;
    }

    @GetMapping("/menu/{code}")
    public String menu(@PathVariable String code) {
        if (accessCodeService.isAccessCodeValid(code)) {
            return "dish/layout/menu";
        } else {
            return "customer/layout/home";
        }
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return "customer/login";
    }

    @GetMapping("/home")
    public String HomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        model.addAttribute("customerLogin", login);
        return "customer/layout/home";
    }

}
