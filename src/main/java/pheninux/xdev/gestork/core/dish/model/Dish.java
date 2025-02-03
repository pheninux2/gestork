package pheninux.xdev.gestork.core.dish.model;

import jakarta.persistence.*;


@Entity
public class Dish {

    public Dish() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private double price;

    private boolean isSpecialPrice;

    @Enumerated(EnumType.STRING)
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

    public void setName(String nom) {
        this.name = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public void setPrice(double prix) {
        this.price = prix;
    }

    public boolean isSpecialPrice() {
        return isSpecialPrice;
    }

    public void setSpecialPrice(boolean specialPrice) {
        isSpecialPrice = specialPrice;
    }

    public DishStatus getStatus() {
        return status;
    }

    public void setStatus(DishStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == DishStatus.AVAILABLE;
    }

    public boolean isNotAvailable() {
        return status == DishStatus.NOT_AVAILABLE;
    }

    public boolean isComingSoon() {
        return status == DishStatus.COMING_SOON;
    }

}
