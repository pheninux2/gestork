package pheninux.xdev.gestork.web.customer;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerLoginPageController {
    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return "customer/login";
    }
}
