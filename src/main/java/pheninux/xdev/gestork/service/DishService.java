package pheninux.xdev.gestork.service;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.Dish;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {

    private final Faker faker;

    public DishService(Faker faker) {
        this.faker = faker;
    }

    public List<Dish> getDishes() {
        return new ArrayList<>(List.of(new Dish("kebab", 12.5),
                new Dish("pizza", 20.5)));
    }

    public List<Dish> getFakeDishes() {
        return new ArrayList<>(List.of(new Dish(faker.food().dish(), 12.0), new Dish(faker.food().dish(), 12.5)));

    }

}
