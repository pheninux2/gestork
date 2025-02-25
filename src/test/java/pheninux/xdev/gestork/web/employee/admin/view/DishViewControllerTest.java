package pheninux.xdev.gestork.web.employee.admin.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.utils.Utils;
import pheninux.xdev.gestork.web.dish.view.DishViewController;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class DishViewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private DishViewController dishViewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dishViewController).build();
    }

    @Test
    void testDisplayAddDishView_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);

            mockMvc.perform(get("/view/dish/add"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("dish/layout/addDish"));
        }
    }

    @Test
    void testDisplayAddDishView_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);

            mockMvc.perform(get("/view/dish/add"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("error/403"));
        }
    }

    @Test
    void testDisplayUpdateDishView_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);

            mockMvc.perform(get("/view/dish/update"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("dish/layout/updateDish"));
        }
    }

    @Test
    void testDisplayUpdateDishView_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);

            mockMvc.perform(get("/view/dish/update"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("error/403"));
        }
    }
}