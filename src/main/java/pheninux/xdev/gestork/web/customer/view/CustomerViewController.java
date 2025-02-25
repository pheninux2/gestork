package pheninux.xdev.gestork.web.customer.view;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/customer")
public class CustomerViewController {

    @GetMapping("/menu")
    public String menu() {
        return "dish/layout/menu";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return "customer/login";
    }

    @GetMapping("/home")
    public String customerHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        model.addAttribute("customerLogin", login);
        return "customer/layout/home";
    }
}
