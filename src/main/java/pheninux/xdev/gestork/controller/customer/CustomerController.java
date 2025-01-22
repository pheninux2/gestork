package pheninux.xdev.gestork.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.exception.CustomServiceException;
import pheninux.xdev.gestork.model.Dish;
import pheninux.xdev.gestork.service.DishService;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final DishService dishService;

    public CustomerController(DishService dishService) {
        this.dishService = dishService;
    }


    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        return "customer/dish/menu";
    }

    @GetMapping("/getDishes")
    public String getDishes(Model model) throws CustomServiceException {
        List<Dish> dishes = dishService.getDishes();
        model.addAttribute("dishes", dishes);
        return "customer/dish/dishesFragment";
    }

}
