
package kz.javaworkshop;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Практическая работа №3 ===");
        System.out.println("Вариант А — Воин");
        System.out.println();

        Hero aliya = new Hero("Алия", 4, 6000);
        Hero daniyar = new Hero("Данияр", 2, 6000);

        Item sword = new Item("Меч", "weapon", 2500, 100);
        Item shield = new Item("Щит", "armor", 3000, 80);
        Item potion = new Item("Зелье", "potion", 500, 20);
        Item feather = new Item("Перо", "potion", 1, 0);
        Item map = new Item("Карта", "armor", 100, 5);
        Item torch = new Item("Факел", "weapon", 200, 10);

        System.out.println("Начальное состояние:");
        printHero(aliya);
        printHero(daniyar);

        System.out.println("\nПодбор Алией:");
        printPickup(aliya, sword);
        printPickup(aliya, shield);
        printPickup(aliya, potion);
        printHero(aliya);

        System.out.println("\nПопытка подобрать перо Алией:");
        Item[] beforeFeather = aliya.items();
        boolean featherResult = aliya.pickUp(feather);
        System.out.println("Алия подбирает Перо: " + featherResult);
        System.out.println("Снимок после отказа сохранён по содержимому: "
                + sameContents(beforeFeather, aliya.items()));
        printHero(aliya);
        printHero(daniyar);

        System.out.println("\nПодбор Данияром:");
        printPickup(daniyar, map);
        printPickup(daniyar, torch);
        printHero(daniyar);

        System.out.println("\nДанияр пытается подобрать перо:");
        Item[] daniyarBefore = daniyar.items();
        boolean daniyarFeather = daniyar.pickUp(feather);
        System.out.println("Данияр подбирает Перо: " + daniyarFeather);
        System.out.println("Снимок после отказа сохранён по содержимому: "
                + sameContents(daniyarBefore, daniyar.items()));

        System.out.println("\nИтоговое состояние:");
        printHero(aliya);
        printHero(daniyar);

        runAdditionalChecks();
    }

    private static void printPickup(Hero hero, Item item) {
        System.out.println(hero.getName() + " подбирает "
                + item.getName() + ": " + hero.pickUp(item));
    }

    private static void printHero(Hero hero) {
        System.out.println(hero.getName() + ": "
                + hero.itemCount() + " предметов, "
                + hero.totalWeightGrams() + " г, "
                + hero.totalValue() + " монет");

        Item[] snapshot = hero.items();
        for (int i = 0; i < snapshot.length; i++) {
            Item item = snapshot[i];
            System.out.println("  " + (i + 1) + ". "
                    + item.getName() + " [" + item.getType() + "], "
                    + item.getWeightGrams() + " г, "
                    + item.getValue() + " монет");
        }
    }

    private static boolean sameContents(Item[] first, Item[] second) {
        if (first.length != second.length) {
            return false;
        }

        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                return false;
            }
        }

        return true;
    }

    private static void check(String description, boolean passed) {
        System.out.println((passed ? "[OK] " : "[ОШИБКА] ")
                + description);
    }

    private static void runAdditionalChecks() {
        System.out.println("\n=== Дополнительные проверки ===");

        // 1. Подбор одним героем не меняет второго.
        Hero first = new Hero("Первый", 2, 500);
        Hero second = new Hero("Второй", 2, 500);
        Item apple = new Item("Яблоко", "potion", 100, 5);

        first.pickUp(apple);
        check("Второй герой остался пустым",
                second.itemCount() == 0
                        && second.totalWeightGrams() == 0);

        // 2. null отклоняется без изменений.
        int countBeforeNull = first.itemCount();
        int weightBeforeNull = first.totalWeightGrams();
        boolean nullResult = first.pickUp(null);

        check("pickUp(null) возвращает false и ничего не меняет",
                !nullResult
                        && first.itemCount() == countBeforeNull
                        && first.totalWeightGrams() == weightBeforeNull);

        // 3. Ровно допустимая масса принимается.
        Hero exact = new Hero("Точный лимит", 1, 100);
        Item exactItem = new Item("Груз", "weapon", 100, 1);

        check("Масса ровно 100 г принимается",
                exact.pickUp(exactItem)
                        && exact.totalWeightGrams() == 100);

        // 4. Масса выше лимита отклоняется.
        Hero overweight = new Hero("Перегруз", 2, 100);
        Item heavy = new Item("Тяжёлый предмет", "armor", 101, 1);

        check("Масса 101 г при лимите 100 г отклоняется",
                !overweight.pickUp(heavy)
                        && overweight.itemCount() == 0);

        // 5. Изменение массива снимка не меняет инвентарь.
        Item[] modifiedSnapshot = first.items();
        modifiedSnapshot[0] = null;

        check("Изменение снимка не меняет рюкзак",
                first.items().length == 1
                        && first.items()[0] == apple);

        // 6. Два снимка — разные массивы с теми же ссылками.
        Item[] snapshotA = first.items();
        Item[] snapshotB = first.items();

        check("Снимки — разные массивы с одинаковой ссылкой",
                snapshotA != snapshotB
                        && snapshotA[0] == snapshotB[0]);

        // 7. Алиас — ссылка на того же героя.
        Hero alias = first;
        Item berry = new Item("Ягода", "potion", 50, 2);
        alias.pickUp(berry);

        check("Подбор через alias виден через first",
                first.itemCount() == 2
                        && first.items()[1] == berry);

        // 8. Общий неизменяемый предмет в двух рюкзаках.
        Hero sharedA = new Hero("Общий А", 2, 500);
        Hero sharedB = new Hero("Общий Б", 2, 500);
        Item sharedItem = new Item("Общий предмет", "weapon", 100, 7);

        boolean acceptedA = sharedA.pickUp(sharedItem);
        boolean acceptedB = sharedB.pickUp(sharedItem);
        Item[] sharedSnapshotA = sharedA.items();
        Item[] sharedSnapshotB = sharedB.items();

        check("Оба героя приняли один Item в независимые массивы",
                acceptedA && acceptedB
                        && sharedSnapshotA != sharedSnapshotB
                        && sharedSnapshotA[0] == sharedSnapshotB[0]);

        // 9. Проверка имени и нормализации.
        check("Имя из пробелов по краям нормализуется",
                new Hero("  Али  ", 1, 100)
                        .getName().equals("Али"));

        check("Имя длиной 1 символ принимается",
                new Hero("А", 1, 100).getName().equals("А"));

        check("Имя длиной 40 символов принимается",
                new Hero("А".repeat(40), 1, 100)
                        .getName().length() == 40);

        check("Пустое имя отклоняется",
                rejectsHero("   "));

        check("Имя длиной 41 символ отклоняется",
                rejectsHero("А".repeat(41)));

        check("null в качестве имени отклоняется",
                rejectsHero(null));

        // 10. Параметры Inventory проверяются его конструктором.
        check("Ёмкость 0 отклоняется",
                rejectsHeroParameters("Тест", 0, 100));

        check("Ёмкость 21 отклоняется",
                rejectsHeroParameters("Тест", 21, 100));

        check("Лимит массы 0 отклоняется",
                rejectsHeroParameters("Тест", 1, 0));

        check("Лимит массы 100001 г отклоняется",
                rejectsHeroParameters("Тест", 1, 100001));
    }

    private static boolean rejectsHero(String name) {
        try {
            new Hero(name, 1, 100);
            return false;
        } catch (IllegalArgumentException exception) {
            return true;
        }
    }

    private static boolean rejectsHeroParameters(
            String name, int capacity, int maxWeightGrams) {
        try {
            new Hero(name, capacity, maxWeightGrams);
            return false;
        } catch (IllegalArgumentException exception) {
            return true;
        }
    }
}
