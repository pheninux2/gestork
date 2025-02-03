package pheninux.xdev.gestork.core.dish.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.repository.DishRepository;
import pheninux.xdev.gestork.exception.CustomServiceException;

import java.util.List;

@Service
public class DishService {

    private final Logger logger = LoggerFactory.getLogger(DishService.class);
    private final DishRepository dishRepository;


    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }


    public void save(Dish dish) {
        try {
            dishRepository.save(dish);
        } catch (Exception e) {
            logger.error("Error while adding dish: {}", e.getMessage());
            throw new CustomServiceException("Error while adding dish: " + e.getMessage(), e, 500);
        }

    }

    public List<Dish> findAll() throws CustomServiceException {
        try {
            return dishRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving dishes: {}", e.getMessage());
            throw new CustomServiceException("Could not retrieve dishes", e, 404);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving dishes: {}", e.getMessage());
            throw new CustomServiceException("Unexpected error while retrieving dishes", e, 500);
        }
    }

    public Dish findById(Long dishId) {
        try {
            return dishRepository.findById(dishId).orElseThrow(() -> new CustomServiceException("Dish not found", 404));
        } catch (Exception e) {
            logger.error("Error while retrieving dish by id: {}", e.getMessage());
            throw new CustomServiceException("Error while retrieving dish by id: " + e.getMessage(), e, 500);
        }
    }

    public void deleteById(Long dishId) {
        dishRepository.deleteById(dishId);
    }
}
