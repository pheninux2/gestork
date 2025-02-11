package pheninux.xdev.gestork.web.employee.admin.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.core.table.service.TableAssignmentService;
import pheninux.xdev.gestork.utils.Utils;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TableApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TableAssignmentService tableAssignmentService;

    @InjectMocks
    private TableApiController tableApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tableApiController).build();
    }

    @Test
    void testAssignTables_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);

            when(tableAssignmentService.assignTables(1L, "1,2,3")).thenReturn(new ResponseEntity<>("Tables assigned", HttpStatus.OK));

            mockMvc.perform(post("/api/admin/table/assignTables")
                            .param("waiter", "1")
                            .param("tables", "1,2,3"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Tables assigned"));
        }
    }

    @Test
    void testAssignTables_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);

            mockMvc.perform(post("/api/admin/table/assignTables")
                            .param("waiter", "1")
                            .param("tables", "1,2,3"))
                    .andExpect(status().isForbidden())
                    .andExpect(redirectedUrl("/error/403"));
        }
    }
}