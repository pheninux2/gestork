package pheninux.xdev.gestork.web.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class CustomerLoginPageControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerLoginPageController customerLoginPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerLoginPageController).build();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/customer/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/login"));
    }
}