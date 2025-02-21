package pheninux.xdev.gestork.web.employee.admin.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.utils.Utils;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pheninux.xdev.gestork.utils.Utils.renderAlertSingle;

class DishApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishApiController dishApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dishApiController).build();
    }

    @Test
    void testAddDish_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {

            mockedUtils.when(Utils::isAdmin).thenReturn(true);
            mockedUtils.when(() -> renderAlertSingle("alert-success", "Le plat est ajouté avec succès.")).thenReturn("Le plat est ajouté avec succès.");
            MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image content".getBytes());

            mockMvc.perform(multipart("/api/admin/dish/add")
                            .file(imageFile)
                            .param("name", "Dish Name")
                            .param("description", "Dish Description")
                            .param("price", "10.0")
                            .param("category", "PLAT")
                            .param("dishStatus", "AVAILABLE"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Le plat est ajouté avec succès.")));
        }
    }

    @Test
    void testAddDish_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);

            MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image content".getBytes());

            mockMvc.perform(multipart("/api/admin/dish/add")
                            .file(imageFile)
                            .param("name", "Dish Name")
                            .param("description", "Dish Description")
                            .param("price", "10.0")
                            .param("category", "MAIN_COURSE")
                            .param("dishStatus", "AVAILABLE"))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void testUpdateDish_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);

            Dish dish = new Dish();
            dish.setDishId(1L);
            when(dishService.findById(1L)).thenReturn(dish);

            mockMvc.perform(post("/api/admin/dish/update/1")
                            .param("newPrice", "15.0")
                            .param("specialPrice", "true"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/fragments/dish/list"));
        }
    }

    @Test
    void testUpdateDish_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);

            mockMvc.perform(post("/api/admin/dish/update/1")
                            .param("newPrice", "15.0")
                            .param("specialPrice", "true"))
                    .andExpect(status().isForbidden())
                    .andExpect(redirectedUrl("/error/403"));
        }
    }

    @Test
    void testDeleteDish_Admin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);

            mockMvc.perform(post("/api/admin/dish/delete/1"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/fragments/dish/list"));
        }
    }

    @Test
    void testDeleteDish_NotAdmin() throws Exception {
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);

            mockMvc.perform(post("/api/admin/dish/delete/1"))
                    .andExpect(status().isForbidden())
                    .andExpect(redirectedUrl("/error/403"));
        }
    }

    @Test
    void testFindAll() throws Exception {
        when(dishService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/dish/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}