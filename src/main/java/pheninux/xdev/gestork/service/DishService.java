package pheninux.xdev.gestork.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.exception.CustomServiceException;
import pheninux.xdev.gestork.model.Dish;
import pheninux.xdev.gestork.repository.DishRepository;

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
        }

    }

    public List<Dish> findAll() throws CustomServiceException {
        try {
            return dishRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving dishes: {}", e.getMessage());
            throw new CustomServiceException("Could not retrieve dishes", e);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving dishes: {}", e.getMessage());
            throw new CustomServiceException("An unexpected error occurred", e);
        }
    }
}
