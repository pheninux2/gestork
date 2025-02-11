package pheninux.xdev.gestork.core.dish.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.repository.DishRepository;
import pheninux.xdev.gestork.exception.CustomServiceException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishServiceTest {

    @InjectMocks
    private DishService dishService;

    @Mock
    private DishRepository dishRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDish() {
        Dish dish = new Dish();
        dish.setName("Test Dish");

        dishService.save(dish);

        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void testSaveDish_Exception() {
        Dish dish = new Dish();
        doThrow(new RuntimeException("Error")).when(dishRepository).save(dish);

        CustomServiceException exception = assertThrows(CustomServiceException.class, () -> dishService.save(dish));

        assertEquals("Error while adding dish: Error", exception.getMessage());
    }

    @Test
    void testFindAll() {
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        when(dishRepository.findAll()).thenReturn(Arrays.asList(dish1, dish2));

        List<Dish> dishes = dishService.findAll();

        assertEquals(2, dishes.size());
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_DataAccessException() {
        when(dishRepository.findAll()).thenThrow(new DataAccessException("DB Error") {});

        CustomServiceException exception = assertThrows(CustomServiceException.class, () -> dishService.findAll());

        assertEquals("Could not retrieve dishes", exception.getMessage());
    }

    @Test
    void testFindById() {
        Dish dish = new Dish();
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Dish foundDish = dishService.findById(1L);

        assertNotNull(foundDish);
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        CustomServiceException exception = assertThrows(CustomServiceException.class, () -> dishService.findById(1L));

        assertEquals("Error while retrieving dish by id: Dish not found", exception.getMessage());
        assertEquals(500, exception.getErrorCode());
    }

    @Test
    void testDeleteById() {
        dishService.deleteById(1L);

        verify(dishRepository, times(1)).deleteById(1L);
    }
}