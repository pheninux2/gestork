package pheninux.xdev.gestork.core.dish.model;


public class DishBuilder {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean isSpecialPrice;
    private Category category;
    private DishStatus status;

    public DishBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public DishBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DishBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public DishBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public DishBuilder setSpecialPrice(boolean isSpecialPrice) {
        this.isSpecialPrice = isSpecialPrice;
        return this;
    }

    public DishBuilder setCategory(Category category) {
        this.category = category;
        return this;
    }

    public DishBuilder setStatus(DishStatus status) {
        this.status = status;
        return this;
    }

    public DishDto build() {
        DishDto dishDto = new DishDto();
        dishDto.setId(this.id);
        dishDto.setName(this.name);
        dishDto.setDescription(this.description);
        dishDto.setPrice(this.price);
        dishDto.setSpecialPrice(this.isSpecialPrice);
        dishDto.setCategory(this.category);
        dishDto.setStatus(this.status);
        return dishDto;
    }

    public static DishBuilder fromDish(Dish dish) {
        return new DishBuilder()
                .setId(dish.getDishId())
                .setName(dish.getName())
                .setDescription(dish.getDescription())
                .setPrice(dish.getPrice())
                .setSpecialPrice(dish.isSpecialPrice())
                .setCategory(dish.getCategory())
                .setStatus(dish.getStatus());
    }
}