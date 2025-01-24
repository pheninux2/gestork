package pheninux.xdev.gestork.controller.employer.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.exception.CustomServiceException;
import pheninux.xdev.gestork.model.Dish;
import pheninux.xdev.gestork.service.DishService;

import java.util.List;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/employee")
public class AdminViewController {

    private final DishService dishService;

    public AdminViewController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/login")
    public String login() {
        return "employee/login";
    }


    @GetMapping("/admin/addDish-page")
    public String displayAddDishPage() {
        isAdmin();
        return "employee/admin/addDish";
    }

    @GetMapping("/admin/manageSpecialPrices")
    public String manageSpecialPrices() {
        isAdmin();
        return "employee/admin/specialPrices";
    }

    @GetMapping("/admin/getDishes")
    public String getDishes(Model model) throws CustomServiceException {
        isAdmin();
        List<Dish> dishes = dishService.findAll();
        model.addAttribute("dishes", dishes);
        return "employee/admin/dishesFragment";
    }

}
