package pheninux.xdev.gestork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.model.Dish;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {


}
