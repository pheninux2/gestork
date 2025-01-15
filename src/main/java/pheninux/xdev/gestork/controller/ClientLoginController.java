package pheninux.xdev.gestork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class ClientLoginController {

    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }

    @GetMapping("/menu")
    public String menu() {
        return "customer/menu";
    }

}
