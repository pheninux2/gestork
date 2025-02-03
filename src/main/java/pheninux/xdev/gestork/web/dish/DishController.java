package pheninux.xdev.gestork.web.dish;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dish")
public class DishController {

    @GetMapping("/menu")
    public String menu() {
        return "dish/layout/menu";
    }

}
