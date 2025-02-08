package pheninux.xdev.gestork.web.employee.admin.view;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;
import pheninux.xdev.gestork.core.employee.model.dto.AssignedTableDto;
import pheninux.xdev.gestork.core.employee.service.EmployeeService;
import pheninux.xdev.gestork.core.table.service.TableAssignmentService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class TableViewController {

    private final EmployeeService employeeService;
    private final TableAssignmentService tableAssignmentService;

    public TableViewController(EmployeeService employeeService, TableAssignmentService tableAssignmentService) {
        this.employeeService = employeeService;
        this.tableAssignmentService = tableAssignmentService;
    }

    @GetMapping("/admin/tables/assign")
    public String showAssignTablesPage(Model model) {
        List<AssignedTableDto> assignedTableDtoList = new ArrayList<>();
        employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name")).forEach(waiter -> {
            AssignedTableDto assignedTableDto = new AssignedTableDto();
            assignedTableDto.setWaiter(waiter);
            assignedTableDto.setTables(tableAssignmentService.getAssignedTableByEmployeeId(waiter.getEmployeeId()));
            assignedTableDtoList.add(assignedTableDto);
        });
        model.addAttribute("employeeAssignedTables", assignedTableDtoList);
        return "employee/admin/layout/assignTables";
    }

}
