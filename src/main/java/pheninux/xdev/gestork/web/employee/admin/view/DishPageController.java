package pheninux.xdev.gestork.web.employee.admin.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.utils.Utils;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/employee")
public class DishPageController {


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
