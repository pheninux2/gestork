package pheninux.xdev.gestork.core.order.builder;

import pheninux.xdev.gestork.core.dish.mapper.DishMapper;
import pheninux.xdev.gestork.core.dish.model.DishDto;
import pheninux.xdev.gestork.core.order.model.OrderDishes;
import pheninux.xdev.gestork.core.order.model.dto.OrderDishesDto;

public class OrderDishesBuilder {

    private DishDto dish;
    private Integer quantity;
    private String comment;


    public OrderDishesBuilder setDish(DishDto dish) {
        this.dish = dish;
        return this;
    }

    public OrderDishesBuilder setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderDishesBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public OrderDishesDto build() {
        OrderDishesDto orderDishesDto = new OrderDishesDto();
        orderDishesDto.setDish(this.dish);
        orderDishesDto.setQuantity(this.quantity);
        orderDishesDto.setComment(this.comment);
        return orderDishesDto;
    }

    public static OrderDishesBuilder fromOrderDishes(OrderDishes orderDishes) {
        return new OrderDishesBuilder()
                .setDish(DishMapper.toDto(orderDishes.getDish()))
                .setQuantity(orderDishes.getQuantity())
                .setComment(orderDishes.getComment());

    }


}
