package pheninux.xdev.gestork.web.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {


    @GetMapping("/login")
    public String login() {
        return "customer/layout/login";
    }

}
