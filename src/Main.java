import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String correctPin = "1234";
        java.io.Console console = System.console();
        String enteredPin;

        if (console != null) {
            char[] pinChars = console.readPassword("Введіть PIN-код для входу (прихований ввід через Console): ");
            enteredPin = new String(pinChars);
        } else {
            System.out.print("Введіть PIN-код для входу (стандартний 1234): ");
            enteredPin = scanner.nextLine();
        }

        if (!enteredPin.equals(correctPin)) {
            System.out.println("Невірний PIN-код. Доступ заборонено.");
            return;
        }
        Storage storage = new Storage();
        ActionLogger logger = new ActionLogger();
        logger.log("Запуск застосунку");
        BackupManager backupManager = new BackupManager();
        TextStorage textStorage = new TextStorage();
        String filename = "my_tasks.dat";

        System.out.println("=== Менеджер завдань ===");

        Project myProject = storage.loadProject(filename);
        if (myProject == null) {
            myProject = new Project("Головний проєкт");
        }

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n[1] Показати завдання (сторінками)");
            System.out.println("[2] Додати нове завдання");
            System.out.println("[3] Позначити як ВИКОНАНО");
            System.out.println("[4] Видалити завдання");
            System.out.println("[5] Зберегти та вийти");
            System.out.println("[6] Демонстрація копіювання (Shallow vs Deep)");
            System.out.println("[7] Згенерувати звіт (instanceof & Record)");
            System.out.println("[8] Безпечний пошук (Optional)");
            System.out.println("[9] Експорт завдань у TXT (Wildcard)");
            System.out.println("[10] Швидке додавання рядком (Методи String)");
            System.out.println("[11] Згенерувати красивий звіт (StringBuilder + PrintWriter)");
            System.out.println("[12] Провести бенчмарк (String vs StringBuilder)");
            System.out.println("[13] Очистити застарілі логи (NIO.2)");
            System.out.println("[14] Створити ZIP-бекап даних");
            System.out.println("[15] Відновити дані з останнього ZIP-бекапу");
            System.out.println("[16] Зберегти дані у TXT-базу (BufferedWriter)");
            System.out.println("[17] Завантажити дані з TXT-бази (BufferedReader)");
            System.out.println("[18] Колекції та власний ітератор (for-each)");
            System.out.println("[19] Сортування списку (Comparable + 2 Comparator)");
            System.out.println("[20] Пошук дедлайнів у TreeMap (найближчий та діапазон)");
            System.out.println("[21] Власне бінарне дерево пошуку (BST)");
            System.out.println("[22] Бенчмарк: ArrayList vs LinkedList");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showTasksPaginated(myProject, scanner);
                    logger.log("Перегляд списку завдань");

                    break;
                case "2":
                    try {
                        String title = "";
                        boolean isTitleValid = false;

                        String titleRegex = "^[^;]+$";

                        while (!isTitleValid) {
                            System.out.print("Назва: ");
                            title = scanner.nextLine();

                            if (title.trim().isEmpty()) {
                                System.out.println("Помилка: Назва не може бути порожньою!");
                            } else if (title.matches(titleRegex)) {
                                isTitleValid = true;
                            } else {
                                System.out.println("Помилка: Назва не може містити символ ';' (він використовується системою).");
                            }
                        }

                        String desc = "";
                        while (desc.trim().isEmpty()) {
                            System.out.print("Опис: ");
                            desc = scanner.nextLine();
                        }

                        String deadline = "";
                        boolean isDeadlineValid = false;

                        String deadlineRegex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";

                        while (!isDeadlineValid) {
                            System.out.print("Дедлайн (ДД.ММ.РРРР): ");
                            deadline = scanner.nextLine();


                            if (deadline.matches(deadlineRegex)) {
                                isDeadlineValid = true;
                            } else {
                                System.out.println("Помилка: Неправильний формат! Використовуйте суворо ДД.ММ.РРРР (наприклад, 14.10.2026).");
                            }
                        }

                        String typeChoice = "";
                        boolean isTypeValid = false;

                        while (!isTypeValid) {
                            System.out.print("Яке це завдання? [1] Звичайне, [2] Термінове: ");
                            typeChoice = scanner.nextLine();

                            try {

                                String typeRegex = "^[12]$";

                                if (!typeChoice.matches(typeRegex)) {
                                    throw new InvalidTaskDataException("Помилка: Невідомий тип завдання. Вводьте лише 1 або 2.");
                                }
                                isTypeValid = true;
                            } catch (InvalidTaskDataException e) {
                                System.out.println(e.getMessage());
                            }
                        }

                        if (typeChoice.equals("1")) {
                            myProject.addTask(new RegularTask(title, desc, deadline, TaskStatus.TODO));
                        } else {
                            myProject.addTask(new UrgentTask(title, desc, deadline, TaskStatus.TODO));
                        }
                        System.out.println("Завдання додано!");
                        logger.log("Додано завдання: " + title);

                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.println("Помилка: Неправильний формат дати! Використовуйте ДД.ММ.РРРР");
                    }
                    break;
                case "3":
                    System.out.print("Введіть номер завдання: ");
                    try {
                        int updateIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (updateIndex >= 0 && updateIndex < myProject.taskRepository.getAll().size()) {
                            myProject.taskRepository.getAll().get(updateIndex).status = TaskStatus.DONE;
                            System.out.println("Статус оновлено на ВИКОНАНО!");
                        } else {
                            System.out.println("Невірний номер.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Будь ласка, введіть число.");
                    }
                    break;
                case "4":
                    System.out.print("Введіть номер завдання для видалення: ");
                    try {
                        int deleteIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        myProject.removeTask(deleteIndex);
                        System.out.println("Завдання видалено!");
                        logger.log("Видалено завдання за індексом " + (deleteIndex + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Будь ласка, введіть коректне число.");
                    } catch (TaskNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "5":
                    storage.saveProject(myProject, filename);
                    logger.log("Збереження та вихід із застосунку");
                    isRunning = false;
                    break;

                case "6":
                    System.out.println("\n--- Демонстрація копіювання ---");

                    Project shallowCopy = new Project(myProject, true);
                    Project deepCopy = new Project(myProject);

                    myProject.addTask(new RegularTask("Тест копіювання", "Опис", "01.01.2027", TaskStatus.TODO));
                    System.out.println("Ми додали тестове завдання в ОРИГІНАЛЬНИЙ проєкт.");

                    System.out.println("Кількість завдань у Поверхневій копії: " + shallowCopy.taskRepository.getAll().size());
                    System.out.println("Кількість завдань у Глибокій копії: " + deepCopy.taskRepository.getAll().size());

                    System.out.println("\nВисновок: Поверхнева копія змінилася разом з оригіналом (бо вони ділять один список).");
                    System.out.println("Глибока копія залишилася недоторканою (бо вона має власний список).");

                    try {
                        myProject.removeTask(myProject.taskRepository.getAll().size() - 1);
                    } catch (TaskNotFoundException e) {

                    }
                    break;

                case "7":
                    int total = myProject.taskRepository.getAll().size();
                    int urgentCount = 0;

                    for (Task t : myProject.taskRepository.getAll()) {
                        if (t instanceof UrgentTask) {
                            urgentCount++;
                        }
                    }

                    TaskReport report = new TaskReport(myProject.name, total, urgentCount);

                    System.out.println("\n--- ЗВІТ ПРОЄКТУ ---");
                    System.out.println("Проєкт: " + report.projectName());
                    System.out.println("Всього завдань: " + report.totalTasks());
                    System.out.println("З них ТЕРМІНОВИХ: " + report.urgentTasksCount());
                    break;
                case "8":
                    System.out.println("\n--- БЕЗПЕЧНИЙ ПОШУК ---");
                    System.out.print("Введіть назву завдання: ");
                    String searchTitle = scanner.nextLine();


                    java.util.Optional<Task> found = myProject.findTaskByTitle(searchTitle);


                    if (found.isPresent()) {
                        System.out.println("Знайдено: " + found.get().title + " (Дедлайн: " + found.get().deadline + ")");
                    } else {
                        System.out.println("Завдання з такою назвою не знайдено.");
                    }
                    break;
                case "9":
                    if (myProject.taskRepository.getAll().isEmpty()) {
                        System.out.println("Список порожній, експортувати нічого.");
                    } else {

                        exportTasksToTxt(myProject.taskRepository.getAll(), "tasks_export.txt");
                    }
                    break;
                case "10":
                    System.out.println("\n--- Швидке додавання (CSV-рядок) ---");
                    System.out.println("Введіть завдання у форматі: Тип; Назва; Опис; Дедлайн");
                    System.out.println("Приклад: термінове; Зробити лабу; Дуже довгий опис; 20/10/2026");
                    System.out.print("Ввід: ");
                    String csvInput = scanner.nextLine();


                    String[] parts = csvInput.split(";");

                    if (parts.length != 4) {
                        System.out.println("Помилка: Неправильний формат. Має бути рівно 4 частини, розділені ';'.");
                        break;
                    }


                    String rawType = parts[0].trim();
                    String rawTitle = parts[1].trim();
                    String rawDesc = parts[2].trim();
                    String rawDate = parts[3].trim();


                    String normalizedType = rawType.toLowerCase();


                    if (rawDesc.length() > 20) {
                        rawDesc = rawDesc.substring(0, 17) + "...";
                    }


                    String safeDate = rawDate.replace("/", ".").replace("-", ".");

                    if (!safeDate.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
                        System.out.println("Помилка: Дата має бути у форматі ДД.ММ.РРРР (навіть після автовиправлення).");
                        break;
                    }


                    if (normalizedType.equals("термінове")) {
                        myProject.addTask(new UrgentTask(rawTitle, rawDesc, safeDate, TaskStatus.TODO));
                        System.out.println("Термінове завдання успішно додано з рядка!");
                    } else if (normalizedType.equals("звичайне")) {
                        myProject.addTask(new RegularTask(rawTitle, rawDesc, safeDate, TaskStatus.TODO));
                        System.out.println("Звичайне завдання успішно додано з рядка!");
                    } else {
                        System.out.println("Помилка: Тип завдання має бути словом 'звичайне' або 'термінове'.");
                    }
                    break;
                case "11":
                    if (myProject.taskRepository.getAll().isEmpty()) {
                        System.out.println("Список порожній, звітувати нічого.");
                        break;
                    }

                    System.out.println("\nГенеруємо звіт...");

                    StringBuilder sb = new StringBuilder();


                    sb.append("==============================================================\n");
                    sb.append(String.format("| %-3s | %-12s | %-12s | %-22s |\n", "ID", "Тип", "Дедлайн", "Назва завдання"));
                    sb.append("--------------------------------------------------------------\n");

                    int counter = 1;
                    for (Task t : myProject.taskRepository.getAll()) {
                        sb.append("| ");
                        sb.append(String.format("%-3d", counter++));
                        sb.append(" | ");
                        sb.append(String.format("%-12s", t.getTaskType()));
                        sb.append(" | ");
                        sb.append(String.format("%-12s", t.deadline));
                        sb.append(" | ");

                        String shortTitle = t.title.length() > 20 ? t.title.substring(0, 19) + "…" : t.title;
                        sb.append(String.format("%-22s", shortTitle));
                        sb.append(" |\n");
                    }

                    sb.append("==============================================================\n");
                    sb.append("Загальна кількість завдань: ").append(myProject.taskRepository.getAll().size()).append("\n");
                    sb.append("Звіт згенеровано автоматично.\n");

                    String finalReport = sb.toString();

                    System.out.println(finalReport);


                    try (java.io.PrintWriter pw = new java.io.PrintWriter("beautiful_report.txt", "UTF-8")) {
                        pw.print(finalReport);
                        System.out.println("Звіт успішно збережено у файл 'beautiful_report.txt'.");
                    } catch (Exception e) {
                        System.out.println("Помилка запису файлу звіту: " + e.getMessage());
                    }
                    break;
                case "12":
                    System.out.println("\n--- Бенчмарк: String (+) vs StringBuilder ---");
                    int iterations = 100_000;
                    System.out.println("Кількість ітерацій: " + iterations);

                    // Тест 1: Звичайний String (+)
                    System.out.println("Тестуємо звичайний String (зачекайте, це може зайняти кілька секунд)...");
                    long startTimeString = System.currentTimeMillis();
                    String testStr = "";
                    for (int i = 0; i < iterations; i++) {
                        testStr += "a";
                    }
                    long endTimeString = System.currentTimeMillis();
                    long durationString = endTimeString - startTimeString;

                    // Тест 2: StringBuilder
                    System.out.println("Тестуємо StringBuilder...");
                    long startTimeBuilder = System.currentTimeMillis();
                    StringBuilder testBuilder = new StringBuilder();
                    for (int i = 0; i < iterations; i++) {
                        testBuilder.append("a"); // Склеюємо через append
                    }
                    long endTimeBuilder = System.currentTimeMillis();
                    long durationBuilder = endTimeBuilder - startTimeBuilder;

                    // Виводимо результати
                    System.out.println("=========================================");
                    System.out.println("Час роботи String (+):    " + durationString + " мс");
                    System.out.println("Час роботи StringBuilder: " + durationBuilder + " мс");
                    System.out.println("=========================================");

                    // Висновок
                    System.out.println("ВИСНОВОК: Оскільки об'єкти String є незмінними, кожна операція '+' створює новий");
                    System.out.println("об'єкт у пам'яті, що призводить до додаткових витрат часу. Клас StringBuilder");
                    System.out.println("модифікує текст всередині існуючого об'єкта, що робить його значно ефективнішим.");
                    break;
                case "13":
                    logger.cleanOldLogs();
                    break;
                case "14":
                    storage.saveProject(myProject, filename);
                    backupManager.createZipBackup(filename, "tasks_db.txt");
                    logger.log("Створено ZIP-бекап даних");
                    break;
                case "15":
                    if (backupManager.restoreFromLatestBackup()) {
                        Project restored = storage.loadProject(filename);
                        if (restored != null) {
                            myProject = restored;
                        }
                        logger.log("Відновлено дані з ZIP-бекапу");
                    }
                    break;
                case "16":
                    textStorage.saveProjectToText(myProject, "tasks_db.txt");
                    logger.log("Збережено проєкт у текстовий файл tasks_db.txt");
                    break;
                case "17":
                    Project textLoaded = textStorage.loadProjectFromText("tasks_db.txt");
                    if (textLoaded != null) {
                        myProject = textLoaded;
                        logger.log("Завантажено проєкт із текстового файлу tasks_db.txt");
                    }
                    break;
                case "18":
                    if (myProject.taskRepository.getAll().isEmpty()) {
                        System.out.println("Список порожній, додайте хоча б кілька завдань.");
                        break;
                    }
                    System.out.println("\n--- Обхід через власний Iterator (for-each) ---");
                    for (Task t : myProject) {
                        System.out.println("- " + t.title + " [" + t.getTaskType() + ", " + t.deadline + "]");
                    }
                    System.out.println("\nSet (унікальні дедлайни): " + myProject.uniqueDeadlines);
                    String firstId = myProject.taskRepository.getAll().get(0).getId();
                    System.out.println("Map (пошук за ID " + firstId + "): " + myProject.findTaskById(firstId).orElse(null));
                    Optional<Task> firstUrgent = myProject.findFirstUrgentTask();
                    if (firstUrgent.isPresent()) {
                        System.out.println("Queue (перше термінове в черзі): " + firstUrgent.get().title);
                    } else {
                        System.out.println("Queue (черга термінових): порожня");
                    }
                    break;

                case "19":
                    if (myProject.taskRepository.getAll().isEmpty()) {
                        System.out.println("Список порожній, сортувати нічого.");
                        break;
                    }
                    List<Task> sortable = new ArrayList<>(myProject.taskRepository.getAll());

                    Collections.sort(sortable);
                    System.out.println("\n--- 1. Comparable (за дедлайном) ---");
                    for (Task t : sortable) {
                        System.out.println(t.deadline + " | " + t.title + " | пріоритет: " + t.getPriorityLevel());
                    }

                    sortable.sort(Task.BY_TITLE);
                    System.out.println("\n--- 2. Comparator (за назвою) ---");
                    for (Task t : sortable) {
                        System.out.println(t.title + " | " + t.deadline + " | пріоритет: " + t.getPriorityLevel());
                    }

                    sortable.sort(Task.BY_PRIORITY);
                    System.out.println("\n--- 3. Comparator (за пріоритетом) ---");
                    for (Task t : sortable) {
                        System.out.println("пріоритет: " + t.getPriorityLevel() + " | " + t.title + " | " + t.deadline);
                    }
                    break;

                case "20":
                    if (myProject.taskRepository.getAll().isEmpty()) {
                        System.out.println("Список порожній.");
                        break;
                    }
                    NavigableMap<LocalDate, List<Task>> byDate = new TreeMap<>();
                    for (Task t : myProject) {
                        LocalDate date = t.getDeadlineDate();
                        if (!date.equals(LocalDate.MAX)) {
                            if (!byDate.containsKey(date)) {
                                byDate.put(date, new ArrayList<>());
                            }
                            byDate.get(date).add(t);
                        }
                    }

                    if (byDate.isEmpty()) {
                        System.out.println("Немає завдань із коректною датою дедлайну.");
                        break;
                    }

                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    try {
                        System.out.print("Введіть дату для пошуку найближчого дедлайну (ДД.ММ.РРРР): ");
                        LocalDate target = LocalDate.parse(scanner.nextLine(), fmt);
                        Map.Entry<LocalDate, List<Task>> nearest = byDate.ceilingEntry(target);
                        if (nearest != null) {
                            System.out.println("Найближчий дедлайн: " + nearest.getKey().format(fmt) + " -> " + nearest.getValue());
                        } else {
                            System.out.println("Після цієї дати завдань немає.");
                        }

                        System.out.print("Початок діапазону (ДД.ММ.РРРР): ");
                        LocalDate from = LocalDate.parse(scanner.nextLine(), fmt);
                        System.out.print("Кінець діапазону (ДД.ММ.РРРР): ");
                        LocalDate to = LocalDate.parse(scanner.nextLine(), fmt);

                        if (from.isAfter(to)) {
                            System.out.println("Початкова дата не може бути пізнішою за кінцеву.");
                            break;
                        }

                        NavigableMap<LocalDate, List<Task>> range = byDate.subMap(from, true, to, true);
                        System.out.println("Завдання у діапазоні:");
                        if (range.isEmpty()) {
                            System.out.println("Нічого не знайдено.");
                        } else {
                            for (Map.Entry<LocalDate, List<Task>> entry : range.entrySet()) {
                                System.out.println(entry.getKey().format(fmt) + ": " + entry.getValue());
                            }
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Неправильний формат дати. Потрібно ДД.ММ.РРРР.");
                    }
                    break;

                case "21":
                    if (myProject.taskRepository.getAll().isEmpty()) {
                        System.out.println("Список порожній, дерево не з чого будувати.");
                        break;
                    }
                    TaskBST bst = new TaskBST();
                    for (Task t : myProject) {
                        bst.insert(t);
                    }
                    System.out.println("\n--- BST: in-order (за алфавітом назв) ---");
                    bst.printInOrder();
                    System.out.println("\n--- BST: pre-order (від кореня) ---");
                    bst.printPreOrder();
                    break;

                case "22":
                    int count = 30_000;
                    System.out.println("\n--- Бенчмарк: ArrayList vs LinkedList (" + count + " елементів у середину) ---");

                    List<Integer> arrayList = new ArrayList<>();
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < count; i++) {
                        arrayList.add(arrayList.size() / 2, i);
                    }
                    long arrayInsert = System.currentTimeMillis() - start;

                    start = System.currentTimeMillis();
                    for (int i = 0; i < count; i++) {
                        arrayList.remove(arrayList.size() / 2);
                    }
                    long arrayRemove = System.currentTimeMillis() - start;

                    List<Integer> linkedList = new LinkedList<>();
                    start = System.currentTimeMillis();
                    for (int i = 0; i < count; i++) {
                        linkedList.add(linkedList.size() / 2, i);
                    }
                    long linkedInsert = System.currentTimeMillis() - start;

                    start = System.currentTimeMillis();
                    for (int i = 0; i < count; i++) {
                        linkedList.remove(linkedList.size() / 2);
                    }
                    long linkedRemove = System.currentTimeMillis() - start;

                    System.out.println("ArrayList  | вставка: " + arrayInsert + " мс | видалення: " + arrayRemove + " мс");
                    System.out.println("LinkedList | вставка: " + linkedInsert + " мс | видалення: " + linkedRemove + " мс");
                    System.out.println("Висновок: при роботі з серединою списку за індексом ArrayList працює швидше,");
                    System.out.println("оскільки має прямий доступ до індексу та швидке зсування масиву, тоді як");
                    System.out.println("LinkedList щоразу проходить половину вузлів від початку або кінця.");
                    break;
                default:
                    System.out.println("Невідома команда. Спробуйте ще раз.");
                    break;

            }

        }
        scanner.close();
    }

    public static void showTasksPaginated(Project project, Scanner scanner) {
        if(project.taskRepository.getAll().isEmpty()) {
            System.out.println(" Список завдань порожній.");
            return;
        }

        int pageSize = 2;
        int totalTasks = project.taskRepository.getAll().size();
        int totalPages = (int) Math.ceil((double) totalTasks / pageSize);

        for (int page = 1; page <= totalPages; page++) {
            System.out.println("\n--- Сторінка " + page + " з " + totalPages + " ---");
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalTasks);

            for (int i = start; i < end; i++) {
                Task t = project.taskRepository.getAll().get(i);
                System.out.println((i + 1) + ". [" + t.status + "] " + t.getTaskType() + ": " + t.title + " (Дедлайн: " + t.deadline + ")");
                System.out.println("   Опис: " + t.description);
            }

            if (page < totalPages) {
                System.out.print("Натисніть Enter, щоб перегорнути на наступну сторінку...");
                scanner.nextLine();
            }
        }
    }
    public static void exportTasksToTxt(java.util.List<? extends Task> tasks, String filename) {

        try (java.io.PrintWriter writer = new java.io.PrintWriter(filename)) {
            writer.println("=== ВАШІ ЗАВДАННЯ ===");
            for (Task t : tasks) {
                writer.println("- [" + t.status + "] " + t.title + " (Дедлайн: " + t.deadline + ")");
            }
            System.out.println("Завдання успішно експортовано у файл: " + filename);
        } catch (java.io.IOException e) {
            System.out.println("Помилка при створенні файлу експорту.");
        }
    }
}