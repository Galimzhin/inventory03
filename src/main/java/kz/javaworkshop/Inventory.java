
package kz.javaworkshop;

import java.util.Arrays;
import java.util.Locale;

public class Inventory {

    private final Item[] items;
    private int size;
    private final int maxWeightGrams;

    public Inventory(int capacity, int maxWeightGrams) {
        if (capacity < 1 || capacity > 20) {
            throw new IllegalArgumentException(
                    "Ёмкость должна быть от 1 до 20");
        }

        if (maxWeightGrams < 1 || maxWeightGrams > 100000) {
            throw new IllegalArgumentException(
                    "Лимит массы должен быть от 1 до 100000 г");
        }

        this.items = new Item[capacity];
        this.size = 0;
        this.maxWeightGrams = maxWeightGrams;
    }

    public boolean add(Item item) {
        if (item == null || size >= items.length) {
            return false;
        }

        long futureWeight =
                (long) totalWeightGrams() + item.getWeightGrams();

        if (futureWeight > maxWeightGrams) {
            return false;
        }

        items[size] = item;
        size++;

        return true;
    }

    public Item remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Item removed = items[index];

        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }

        size--;
        items[size] = null;

        return removed;
    }

    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        return items[index];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return items.length;
    }

    public int getMaxWeightGrams() {
        return maxWeightGrams;
    }

    public int totalWeightGrams() {
        int total = 0;

        for (int i = 0; i < size; i++) {
            total += items[i].getWeightGrams();
        }

        return total;
    }

    public long totalValue() {
        long total = 0;

        for (int i = 0; i < size; i++) {
            total += items[i].getValue();
        }

        return total;
    }

    public Item[] snapshot() {
        return Arrays.copyOf(items, size);
    }

    public Item[] findByName(String query) {
        if (query == null) {
            throw new IllegalArgumentException(
                    "Поисковый запрос не должен быть null");
        }

        String normalizedQuery =
                query.strip().toLowerCase(Locale.ROOT);

        Item[] matches = new Item[size];
        int count = 0;

        for (int i = 0; i < size; i++) {
            String normalizedName =
                    items[i].getName().toLowerCase(Locale.ROOT);

            if (normalizedName.contains(normalizedQuery)) {
                matches[count] = items[i];
                count++;
            }
        }

        return Arrays.copyOf(matches, count);
    }
}
