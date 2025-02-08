package pheninux.xdev.gestork.web.employee;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "employee/login";
    }
}
