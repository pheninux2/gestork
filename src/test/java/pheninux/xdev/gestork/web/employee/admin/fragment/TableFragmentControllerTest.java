package pheninux.xdev.gestork.web.employee.admin.fragment;

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
import pheninux.xdev.gestork.web.table.fragment.TableFragmentController;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class TableFragmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private TableAssignmentService tableAssignmentService;

    @InjectMocks
    private TableFragmentController tableFragmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tableFragmentController).build();
    }

    @Test
    void testAssignTablesList() throws Exception {
        when(employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name"))).thenReturn(List.of());

        mockMvc.perform(get("/fragment/table/assignTablesForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("table/fragment/assignTablesForm"))
                .andExpect(model().attributeExists("waiters"));
    }

    @Test
    void testGetAssignedTables() throws Exception {
        Employee waiter = new Employee();
        waiter.setEmployeeId(1L);
        waiter.setRole(EmployeeRole.WAITER);

        AssignedTableDto assignedTableDto = new AssignedTableDto();
        assignedTableDto.setWaiter(waiter);
        assignedTableDto.setTables(Collections.emptyList());

        when(employeeService.getEmployeesByRole(EmployeeRole.WAITER, Sort.by("name"))).thenReturn(List.of(waiter));
        when(tableAssignmentService.getAssignedTableByEmployeeId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/fragment/table/assignedTablesList"))
                .andExpect(status().isOk())
                .andExpect(view().name("table/fragment/assignedTablesList"))
                .andExpect(model().attributeExists("employeeAssignedTables"));
    }
}