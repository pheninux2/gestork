package pheninux.xdev.gestork.web.employee.admin.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.model.EmployeeRole;
import pheninux.xdev.gestork.core.employee.model.dto.AssignedTableDto;
import pheninux.xdev.gestork.core.employee.service.EmployeeService;
import pheninux.xdev.gestork.core.table.service.TableAssignmentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TablePageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private TableAssignmentService tableAssignmentService;

    @InjectMocks
    private TablePageController tablePageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tablePageController).build();
    }

    @Test
    void testShowAssignTablesPage() throws Exception {
        Employee waiter = new Employee();
        waiter.setEmployeeId(1L);
        waiter.setRole(EmployeeRole.WAITER);

        AssignedTableDto assignedTableDto = new AssignedTableDto();
        assignedTableDto.setWaiter(waiter);
        assignedTableDto.setTables(Collections.emptyList());

        when(employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name"))).thenReturn(List.of(waiter));
        when(tableAssignmentService.getAssignedTableByEmployeeId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employee/admin/tables/assign"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/admin/layout/assignTables"))
                .andExpect(model().attributeExists("employeeAssignedTables"));
    }
}