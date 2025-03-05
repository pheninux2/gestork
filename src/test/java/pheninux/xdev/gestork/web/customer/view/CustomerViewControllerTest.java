package pheninux.xdev.gestork.web.customer.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.core.accessCode.repository.AccessCodeRepository;
import pheninux.xdev.gestork.core.accessCode.service.AccessCodeService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class CustomerViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccessCodeService accessCodeService;

    @InjectMocks
    private CustomerViewController customerViewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerViewController).build();
    }

    @Test
    void testMenuWithValidCode() throws Exception {
        String code = "validCode";
        when(accessCodeService.isAccessCodeValid(code)).thenReturn(true);

        mockMvc.perform(get("/view/customer/menu/" + code))
                .andExpect(status().isOk())
                .andExpect(view().name("dish/layout/menu"));
    }

    @Test
    void testMenuWithInvalidCode() throws Exception {
        String code = "invalidCode";
        when(accessCodeService.isAccessCodeValid(code)).thenReturn(false);

        mockMvc.perform(get("/view/customer/menu/" + code))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/layout/home"));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/view/customer/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/login"));
    }
}