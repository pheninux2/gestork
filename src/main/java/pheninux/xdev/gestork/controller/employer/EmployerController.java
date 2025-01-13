package pheninux.xdev.gestork.controller.employer;

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

    @GetMapping("/generateCode-page")
    public String generateCodePage() {
        return "employee/waiter/services/generate-code";
    }

    @GetMapping("/showAccessCode-page")
    public String showAccessCode(@RequestParam("code") String code, Model model) {
        model.addAttribute("accessCode", code);
        return "employee/waiter/show-access-code";
    }

}
