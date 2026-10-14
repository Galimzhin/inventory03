
package kz.javaworkshop;

import java.util.Scanner;

public class Main {

    private static final int CAPACITY = 4;
    private static final int MAX_WEIGHT = 6000;

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine();
    }

    private static Integer readInteger(
            Scanner scanner, String prompt) {

        while (true) {
            String line = readLine(scanner, prompt);

            if (line == null) {
                return null;
            }

            try {
                return Integer.parseInt(line.strip());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Ошибка: введите целое число.");
            }
        }
    }

    private static void showItems(Inventory inventory) {
        System.out.println("\n--- Содержимое рюкзака ---");

        if (inventory.size() == 0) {
            System.out.println("Рюкзак пуст.");
            return;
        }

        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + inventory.get(i));
        }
    }

    private static void showSummary(Inventory inventory) {
        System.out.println("\n--- Сводка инвентаря ---");
        System.out.println(
                "Ячейки: " + inventory.size()
                        + "/" + inventory.capacity());
        System.out.println(
                "Масса: " + inventory.totalWeightGrams()
                        + "/" + inventory.getMaxWeightGrams()
                        + " г");
        System.out.println(
                "Стоимость: " + inventory.totalValue()
                        + " монет");
    }

    private static boolean addItem(
            Scanner scanner, Inventory inventory) {

        String name = readLine(scanner, "Название предмета: ");

        if (name == null) {
            return false;
        }

        String type = readLine(
                scanner, "Тип (weapon, armor, potion): ");

        if (type == null) {
            return false;
        }

        type = type.strip();

        Integer weight = readInteger(
                scanner, "Масса в граммах: ");

        if (weight == null) {
            return false;
        }

        Integer value = readInteger(
                scanner, "Стоимость в монетах: ");

        if (value == null) {
            return false;
        }

        try {
            Item item = new Item(name, type, weight, value);

            if (inventory.add(item)) {
                System.out.println("Добавлено: " + item);
            } else {
                System.out.println(
                        "Отказ: нет свободных ячеек "
                                + "или превышен лимит массы.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Некорректные данные: " + e.getMessage());
        }

        return true;
    }

    private static boolean removeItem(
            Scanner scanner, Inventory inventory) {

        Integer number = readInteger(
                scanner, "Номер предмета для удаления: ");

        if (number == null) {
            return false;
        }

        if (number < 1 || number > inventory.size()) {
            System.out.println("Неверный номер предмета.");
            return true;
        }

        Item removed = inventory.remove(number - 1);
        System.out.println("Удалено: " + removed);

        return true;
    }

    private static boolean usePotion(
            Scanner scanner, Inventory inventory) {

        Integer number = readInteger(
                scanner, "Номер предмета для использования: ");

        if (number == null) {
            return false;
        }

        if (number < 1 || number > inventory.size()) {
            System.out.println("Неверный номер предмета.");
            return true;
        }

        Item item = inventory.get(number - 1);

        if (!item.getType().equals("potion")) {
            System.out.println(
                    "Этот предмет не является зельем.");
            return true;
        }

        inventory.remove(number - 1);

        System.out.println("Зелье использовано: " + item.getName());

        return true;
    }

    private static boolean searchItems(
            Scanner scanner, Inventory inventory) {

        String query = readLine(
                scanner, "Введите имя или часть имени: ");

        if (query == null) {
            return false;
        }

        try {
            Item[] found = inventory.findByName(query);

            if (found.length == 0) {
                System.out.println("Ничего не найдено.");
            } else {
                System.out.println("Результаты поиска:");

                for (int i = 0; i < found.length; i++) {
                    System.out.println(
                            (i + 1) + ". " + found[i]);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    private static void showMenu() {
        System.out.println("\n========== ИНВЕНТАРЬ ==========");
        System.out.println("1 — Добавить предмет");
        System.out.println("2 — Показать предметы");
        System.out.println("3 — Удалить предмет");
        System.out.println("4 — Использовать зелье");
        System.out.println("5 — Сводка инвентаря");
        System.out.println("6 — Поиск по имени");
        System.out.println("0 — Выход");
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Inventory inventory =
                    new Inventory(CAPACITY, MAX_WEIGHT);

            // Вариант А — Воин
            inventory.add(new Item("Меч", "weapon", 2500, 100));
            inventory.add(new Item("Щит", "armor", 3000, 80));
            inventory.add(new Item("Зелье", "potion", 500, 20));

            System.out.println("Практическая работа №2");
            System.out.println("Инвентарь героя — вариант А: Воин");

            showItems(inventory);
            showSummary(inventory);

            boolean running = true;

            while (running) {
                showMenu();

                Integer command = readInteger(
                        scanner, "Выберите команду: ");

                if (command == null) {
                    System.out.println(
                            "\nВвод завершён. Программа остановлена.");
                    break;
                }

                switch (command) {
                    case 1:
                        if (!addItem(scanner, inventory)) {
                            running = false;
                        }
                        break;

                    case 2:
                        showItems(inventory);
                        break;

                    case 3:
                        if (!removeItem(scanner, inventory)) {
                            running = false;
                        }
                        break;

                    case 4:
                        if (!usePotion(scanner, inventory)) {
                            running = false;
                        }
                        break;

                    case 5:
                        showSummary(inventory);
                        break;

                    case 6:
                        if (!searchItems(scanner, inventory)) {
                            running = false;
                        }
                        break;

                    case 0:
                        running = false;
                        System.out.println("Программа завершена.");
                        break;

                    default:
                        System.out.println(
                                "Неизвестная команда. "
                                        + "Введите число от 0 до 6.");
                }
            }
        }
    }
}
