package pheninux.xdev.gestork.controller.employer.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.exception.CustomServiceException;
import pheninux.xdev.gestork.model.Dish;
import pheninux.xdev.gestork.service.DishService;
import pheninux.xdev.gestork.utils.Utils;

import java.util.List;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/employee")
public class AdminViewController {

    private final DishService dishService;

    public AdminViewController(DishService dishService) {
        this.dishService = dishService;
    }


    @GetMapping("/admin/add-dish")
    public String displayAddDishPage() {
        if (!Utils.isAdmin()) {
            return "redirect:error/403";
        }
        return "employee/admin/add-dish";
    }

    @GetMapping("/admin/special-prices")
    public String manageSpecialPrices() {
        if (!isAdmin()) {
            return "redirect:error/403";
        }
        return "employee/admin/special-prices";
    }

    @GetMapping("/admin/dishes")
    public String getDishes(Model model) throws CustomServiceException {
        if (!isAdmin()) {
            return "redirect:error/403";
        }
        List<Dish> dishes = dishService.findAll();
        model.addAttribute("dishes", dishes);
        return "employee/admin/dishes-fragment";
    }

}
