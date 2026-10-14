
package kz.javaworkshop;

public class Hero {
    private final String name;
    private final Inventory inventory;

    public Hero(String name, int capacity, int maxWeightGrams) {
        if (name == null) {
            throw new IllegalArgumentException(
                    "Имя героя не должно быть null"
            );
        }

        String normalizedName = name.strip();

        if (normalizedName.length() < 1
                || normalizedName.length() > 40) {
            throw new IllegalArgumentException(
                    "Имя героя должно содержать от 1 до 40 символов"
            );
        }

        this.name = normalizedName;
        this.inventory = new Inventory(capacity, maxWeightGrams);
    }

    public String getName() {
        return name;
    }

    public boolean pickUp(Item item) {
        return inventory.add(item);
    }

    public int itemCount() {
        return inventory.size();
    }

    public int totalWeightGrams() {
        return inventory.totalWeightGrams();
    }

    public long totalValue() {
        return inventory.totalValue();
    }

    public Item[] items() {
        return inventory.snapshot();
    }
}
