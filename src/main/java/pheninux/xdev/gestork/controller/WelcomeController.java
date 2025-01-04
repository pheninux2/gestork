package pheninux.xdev.gestork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pheninux.xdev.gestork.service.DishService;

@Controller
public class WelcomeController {
    @Autowired
    DishService dishService;

    @GetMapping(value = "/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/dish")
    public String dish(Model model) {
        model.addAttribute("dishes", dishService.getFakeDishes());
        return "dish";
    }

}
