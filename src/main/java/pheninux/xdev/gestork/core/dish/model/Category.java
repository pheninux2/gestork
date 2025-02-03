package pheninux.xdev.gestork.core.dish.model;

public enum Category {
    PLAT_CHEF("Plat du Chef"),
    PLAT("Plat"),
    ENTREE_CHAUDE("Entrée Chaude"),
    ENTREE_FROIDE("Entrée Froide"),
    BOISSON_CHAUDE("Boisson Chaude"),
    BOISSON_FROIDE("Boisson Froide"),
    DESSERT("Dessert");
    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Aucune catégorie correspondante pour: " + text);
    }
}
