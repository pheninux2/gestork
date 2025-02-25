package pheninux.xdev.gestork.core.order.model.dto;

import pheninux.xdev.gestork.core.dish.model.DishDto;

public class OrderDishesDto {


    private DishDto dish;
    private Integer quantity;
    private String comment;

    public DishDto getDish() {
        return dish;
    }

    public void setDish(DishDto dish) {
        this.dish = dish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
