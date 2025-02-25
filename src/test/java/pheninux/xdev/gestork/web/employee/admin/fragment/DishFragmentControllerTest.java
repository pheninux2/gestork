package pheninux.xdev.gestork.web.employee.admin.fragment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.utils.Utils;
import pheninux.xdev.gestork.web.dish.fragment.DishFragmentController;

import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DishFragmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishFragmentController dishFragmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dishFragmentController).build();
    }

    @Test
    void testGetDishListFragment_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);
            when(dishService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/fragment/dish/list"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("dish/fragment/adminDishes"))
                    .andExpect(model().attributeExists("dishes"));
        }
    }

    @Test
    void testGetDishListFragment_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);
            when(dishService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/fragment/dish/list"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("dish/fragment/menuDishes"))
                    .andExpect(model().attributeExists("dishes"));
        }
    }

    @Test
    void testGetDishFormFragment() throws Exception {
        mockMvc.perform(get("/fragment/dish/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("dish/fragment/dishForm"))
                .andExpect(model().attributeExists("dish"));
    }
}