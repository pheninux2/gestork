package pheninux.xdev.gestork.web.customer.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class LoginViewController {
    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }
}
