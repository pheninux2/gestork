package pheninux.xdev.gestork.web.dish.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.utils.Utils;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/view/dish")
public class DishViewController {


    @GetMapping("/add")
    public String displayAddDishView() {
        if (!Utils.isAdmin()) {
            return "redirect:error/403";
        }
        return "dish/layout/addDish";
    }

    @GetMapping("/update")
    public String displayUpdateDishView() {
        if (!isAdmin()) {
            return "redirect:error/403";
        }
        return "dish/layout/updateDish";
    }





}
