package pheninux.xdev.gestork.web.employee.admin;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;
import pheninux.xdev.gestork.core.employee.service.EmployeeService;
import pheninux.xdev.gestork.utils.Utils;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/employee")
public class AdminViewController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public AdminViewController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }


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


    @GetMapping("/admin/assignTables")
    public String showAssignTablesPage(Model model) {
        model.addAttribute("waiters", employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name")));
        return "employee/admin/layout/assignTables";
    }

}
