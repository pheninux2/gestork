package pheninux.xdev.gestork.model;

public enum DishStatus {

    AVAILABLE("Disponible"),
    NOT_AVAILABLE("Non disponible"),
    COMING_SOON("À venir");

    private final String description;

    DishStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}