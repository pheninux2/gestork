package pheninux.xdev.gestork.core.dish.model;

public class DishDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean isSpecialPrice;
    private Category category;
    private DishStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSpecialPrice() {
        return isSpecialPrice;
    }

    public void setSpecialPrice(boolean specialPrice) {
        isSpecialPrice = specialPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public DishStatus getStatus() {
        return status;
    }

    public void setStatus(DishStatus status) {
        this.status = status;
    }

}
