package pheninux.xdev.gestork.web.customer.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class CustomerViewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerViewController customerViewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerViewController).build();
    }

    @Test
    void testMenu() throws Exception {
        mockMvc.perform(get("/view/customer/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("dish/layout/menu"));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/view/customer/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/login"));
    }
}