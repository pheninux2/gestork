package pheninux.xdev.gestork.web.employee.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pheninux.xdev.gestork.core.table.service.EmployeeTableAssignmentService;

import java.net.URI;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/employee/admin/table")
public class AdminTableController {

    private final EmployeeTableAssignmentService employeeTableAssignmentService;

    public AdminTableController(EmployeeTableAssignmentService employeeTableAssignmentService) {
        this.employeeTableAssignmentService = employeeTableAssignmentService;
    }

    @PostMapping("/assignTables")
    public ResponseEntity<String> assignTables(@RequestParam("waiter") Long waiterId, @RequestParam("tables") String tables) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .location(URI.create("/error/403"))
                    .build();
        }
        return employeeTableAssignmentService.assignEmployeeToTable(waiterId, tables);

    }
}