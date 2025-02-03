package pheninux.xdev.gestork.web.employee.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.exception.CustomServiceException;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.utils.Utils;

import java.util.List;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/employee")
public class AdminViewController {


    @GetMapping("/admin/dish/add")
    public String displayAddDishView() {
        if (!Utils.isAdmin()) {
            return "redirect:error/403";
        }
        return "employee/admin/layout/addDish";
    }

    @GetMapping("/admin/dish/update")
    public String displayUpdateDishView() {
        if (!isAdmin()) {
            return "redirect:error/403";
        }
        return "employee/admin/layout/updateDish";
    }

}
