package pheninux.xdev.gestork.web.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.web.employee.view.LoginViewController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class EmployeeViewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LoginViewController loginViewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginViewController).build();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/view/employee/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/login"));
    }
}