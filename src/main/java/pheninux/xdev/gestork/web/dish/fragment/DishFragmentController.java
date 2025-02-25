package pheninux.xdev.gestork.web.dish.fragment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.service.DishService;

import java.util.List;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/fragment/dish")
public class DishFragmentController {

    private final DishService dishService;

    public DishFragmentController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/list")
    public String getDishListFragment(Model model) {
        List<Dish> dishes = dishService.findAll();

        model.addAttribute("dishes", dishService.findAll());
        if (isAdmin()) {
            return "dish/fragment/adminDishes";
        }

        return "dish/fragment/menuDishes";
    }

    @GetMapping("/form")
    public String getDishFormFragment(Model model) {
        model.addAttribute("dish", new Dish());

        return "dish/fragment/dishForm";
    }

}