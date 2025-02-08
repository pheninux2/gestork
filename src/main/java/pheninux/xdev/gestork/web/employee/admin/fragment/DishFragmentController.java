package pheninux.xdev.gestork.web.employee.admin.fragment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.service.DishService;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/fragments/dish")
public class DishFragmentController {

    private final DishService dishService;

    public DishFragmentController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/list")
    public String getDishListFragment(Model model) {
        model.addAttribute("dishes", dishService.findAll());
        if (isAdmin()) {
            return "dish/fragments/adminDishes";
        }

        return "dish/fragments/menuDishes";
    }

    @GetMapping("/form")
    public String getDishFormFragment(Model model) {
        model.addAttribute("dish", new Dish());

        return "dish/fragments/dishForm";
    }

}