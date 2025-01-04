package pheninux.xdev.gestork.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/client")
@Controller
public class ClientLoginController {

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/login")
    public String login() {
        return "client/login";
    }
}
