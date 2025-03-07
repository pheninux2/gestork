package pheninux.xdev.gestork.web.employee.waiter.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.utils.Utils;
import pheninux.xdev.gestork.web.accesscode.view.CodeViewController;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CodeViewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CodeViewController codeViewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(codeViewController).build();
    }

    @Test
    void testDisplayGenerateCodePage_Admin() throws Exception {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);
            mockedUtils.when(Utils::isWaiter).thenReturn(false);

            mockMvc.perform(get("/view/code/generate"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employee/waiter/layout/generateCode"));
        }
    }

    @Test
    void testDisplayGenerateCodePage_Waiter() throws Exception {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);
            mockedUtils.when(Utils::isWaiter).thenReturn(true);

            mockMvc.perform(get("/view/code/generate"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employee/waiter/layout/generateCode"));
        }
    }

    @Test
    void testDisplayGenerateCodePage_Forbidden() throws Exception {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);
            mockedUtils.when(Utils::isWaiter).thenReturn(false);

            mockMvc.perform(get("/view/code/generate"))
                    .andExpect(status().isForbidden())
                    .andExpect(view().name("error/403"));
        }
    }

    @Test
    void testDisplayCodePage() throws Exception {
        String code = "12345";

        mockMvc.perform(get("/view/code/display").param("code", code))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/waiter/layout/displayCode"))
                .andExpect(model().attribute("accessCode", code));
    }
}