package pheninux.xdev.gestork.core.dish.mapper;

import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.model.DishBuilder;
import pheninux.xdev.gestork.core.dish.model.DishDto;

import java.util.List;
import java.util.stream.Collectors;

public class DishMapper {


    public static List<DishDto> toDtoList(List<Dish> dishes) {
        return dishes.stream().map(DishMapper::toDto).collect(Collectors.toList());
    }

    public static DishDto toDto(Dish dish) {
        return DishBuilder.fromDish(dish).build();
    }
}
