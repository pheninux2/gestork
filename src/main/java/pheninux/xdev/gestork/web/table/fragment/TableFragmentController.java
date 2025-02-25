package pheninux.xdev.gestork.web.table.fragment;

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
@RequestMapping("/fragment/table")
public class TableFragmentController {

    private final TableAssignmentService tableAssignmentService;
    private final EmployeeService employeeService;


    public TableFragmentController(TableAssignmentService tableAssignmentService, EmployeeService employeeService) {
        this.tableAssignmentService = tableAssignmentService;
        this.employeeService = employeeService;
    }

    @GetMapping("/assignTablesForm")
    public String assignTablesList(Model model) {
        model.addAttribute("waiters", employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name")));
        return "table/fragment/assignTablesForm";
    }

    @GetMapping("/assignedTablesList")
    public String getAssignedTables(Model model) {
        List<AssignedTableDto> assignedTableDtoList = new ArrayList<>();
        employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name")).forEach(waiter -> {
            AssignedTableDto assignedTableDto = new AssignedTableDto();
            assignedTableDto.setWaiter(waiter);
            assignedTableDto.setTables(tableAssignmentService.getAssignedTableByEmployeeId(waiter.getEmployeeId()));
            assignedTableDtoList.add(assignedTableDto);
        });
        model.addAttribute("employeeAssignedTables", assignedTableDtoList);
        return "table/fragment/assignedTablesList";
    }
}
