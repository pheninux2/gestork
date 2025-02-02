package pheninux.xdev.gestork.core.dish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.core.dish.model.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {


}
