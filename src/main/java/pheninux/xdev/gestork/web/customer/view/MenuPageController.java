package pheninux.xdev.gestork.web.customer.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class MenuPageController {

    @GetMapping("/menu")
    public String menu() {
        return "dish/layout/menu";
    }
}
