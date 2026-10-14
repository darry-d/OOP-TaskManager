import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage();
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
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showTasksPaginated(myProject, scanner);

                    break;
                case "2":
                    try {
                        String title = "";
                        while (title.trim().isEmpty()) {
                            System.out.print("Назва: ");
                            title = scanner.nextLine();
                        }

                        String desc = "";
                        while (desc.trim().isEmpty()) {
                            System.out.print("Опис: ");
                            desc = scanner.nextLine();
                        }

                        System.out.print("Дедлайн (ДД.ММ.РРРР): ");
                        String deadline = scanner.nextLine();


                        String typeChoice = "";
                        boolean isTypeValid = false;

                        while (!isTypeValid) {
                            System.out.print("Яке це завдання? [1] Звичайне, [2] Термінове: ");
                            typeChoice = scanner.nextLine();

                            try {
                                if (!typeChoice.equals("1") && !typeChoice.equals("2")) {
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
                    } catch (NumberFormatException e) {
                        System.out.println("Будь ласка, введіть коректне число.");
                    } catch (TaskNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "5":
                    storage.saveProject(myProject, filename);
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