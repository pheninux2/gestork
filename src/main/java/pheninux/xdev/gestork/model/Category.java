package pheninux.xdev.gestork.model;

public enum Category {
    PLAT("Plat"),
    ENTREE_CHAUDE("Entrée Chaude"),
    ENTREE_FROIDE("Entrée Froide"),
    DESSERT("Dessert"),
    BOISSON_CHAUDE("Boisson Chaude"),
    BOISSON_FROIDE("Boisson Froide"),
    PLAT_CHEF("Plat du Chef");

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
