package pheninux.xdev.gestork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeLoginController {

    @GetMapping("/login")
    public String login() {
        return "employee/login";
    }

}
