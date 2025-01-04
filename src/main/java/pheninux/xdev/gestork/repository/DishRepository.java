package pheninux.xdev.gestork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.Dish;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    default List<Dish> buildDishes() {
        return new ArrayList<>(List.of(new Dish("pizza", 12.5),
                new Dish("kebab", 13.5)));
    }
}
