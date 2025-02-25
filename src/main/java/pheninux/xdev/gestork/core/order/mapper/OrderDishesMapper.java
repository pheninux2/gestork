package pheninux.xdev.gestork.core.order.mapper;

import pheninux.xdev.gestork.core.order.builder.OrderDishesBuilder;
import pheninux.xdev.gestork.core.order.model.OrderDishes;
import pheninux.xdev.gestork.core.order.model.dto.OrderDishesDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDishesMapper {

    public static List<OrderDishesDto> toDtoList(List<OrderDishes> orderDishes) {
        return orderDishes.stream().map(OrderDishesMapper::toDto).collect(Collectors.toList());
    }

    public static OrderDishesDto toDto(OrderDishes orderDishes) {
        return OrderDishesBuilder.fromOrderDishes(orderDishes).build();
    }
}
