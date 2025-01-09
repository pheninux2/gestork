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

//    @PostMapping("/authenticate")
//    public String authenticate(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
//        model.addAttribute("username", username);
//        model.addAttribute("password", password);
//        return "client/home";
//    }

}
