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
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();


            switch (choice) {
                case "1":
                    showTasksPaginated(myProject, scanner);
                    break;
                case "2":
                    String title = "";
                   
                    while (title.trim().isEmpty()) {
                        System.out.print("Назва: ");
                        title = scanner.nextLine();
                        if (title.trim().isEmpty()) {
                            System.out.println("Помилка: Назва не може бути порожньою.");
                        }
                    }

                    String desc = "";
                    while (desc.trim().isEmpty()) {
                        System.out.print("Опис: ");
                        desc = scanner.nextLine();
                        if (desc.trim().isEmpty()) {
                            System.out.println("Помилка: Опис не може бути порожнім.");
                        }
                    }

                    String deadline = "";
                    java.time.LocalDate validDate = null;
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    while (validDate == null) {
                        System.out.print("Дедлайн (у форматі ДД.ММ.РРРР): ");
                        String inputDate = scanner.nextLine();
                        try {
                            validDate = java.time.LocalDate.parse(inputDate, formatter);

                            if (validDate.isBefore(java.time.LocalDate.now())) {
                                System.out.println("Дедлайн не може бути в минулому. Введіть сьогоднішню або майбутню дату.");
                                validDate = null;


                            } else {
                                deadline = inputDate;

                            }

                        } catch (java.time.format.DateTimeParseException e ) {
                            System.out.println("Неправильний формат. Використовуйте ДД.ММ.РРРР (наприклад, 26.09.2026).");
                        }
                    }
                    String taskTypeChoice = "";

                    while (!taskTypeChoice.equals("1") && !taskTypeChoice.equals("2")) {
                        System.out.print("Яке це завдання? [1] Звичайне, [2] Термінове: ");
                        taskTypeChoice = scanner.nextLine().trim();

                        if (!taskTypeChoice.equals("1") && !taskTypeChoice.equals("2")) {
                            System.out.println("Помилка: Введіть лише 1 або 2.");
                        }
                    }

                    if (taskTypeChoice.equals("2")) {
                        myProject.addTask(new UrgentTask(title, desc, deadline, TaskStatus.TODO));
                    } else {
                        myProject.addTask(new RegularTask(title, desc, deadline, TaskStatus.TODO));
                    }
                    System.out.println("Завдання додано!");
                    break;
                case "3":
                    System.out.print("Введіть номер завдання: ");
                    try {
                        int updateIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (updateIndex >= 0 && updateIndex < myProject.tasks.size()) {
                            myProject.tasks.get(updateIndex).status = TaskStatus.DONE;
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
                        if (deleteIndex >= 0 && deleteIndex < myProject.tasks.size()) {
                            myProject.tasks.remove(deleteIndex);
                            System.out.println("Завдання видалено!");
                        } else {
                            System.out.println("Невірний номер.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Будь ласка, введіть число.");
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

                    System.out.println("Кількість завдань у Поверхневій копії: " + shallowCopy.tasks.size());
                    System.out.println("Кількість завдань у Глибокій копії: " + deepCopy.tasks.size());

                    System.out.println("\nВисновок: Поверхнева копія змінилася разом з оригіналом (бо вони ділять один список).");
                    System.out.println("Глибока копія залишилася недоторканою (бо вона має власний список).");

                    myProject.tasks.remove(myProject.tasks.size() - 1);
                    break;

                case "7":
                    int total = myProject.tasks.size();
                    int urgentCount = 0;

                    for (Task t : myProject.tasks) {
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

                default:
                    System.out.println("Невідома команда. Спробуйте ще раз.");
                    break;
            }

        }
        scanner.close();
    }

    public static void showTasksPaginated(Project project, Scanner scanner) {
        if(project.tasks.isEmpty()) {
            System.out.println(" Список завдань порожній.");
            return;
        }

        int pageSize = 2; // Кількість завдань на одній сторінці
        int totalTasks = project.tasks.size();
        int totalPages = (int) Math.ceil((double) totalTasks / pageSize);

        for (int page = 1; page <= totalPages; page++) {
            System.out.println("\n--- Сторінка " + page + " з " + totalPages + " ---");
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalTasks);

            for (int i = start; i < end; i++) {
                Task t = project.tasks.get(i);
                System.out.println((i + 1) + ". [" + t.status + "] " + t.getTaskType() + ": " + t.title + " (Пріоритет: " + t.getPriorityLevel() + ", Дедлайн: " + t.deadline + ")");
                System.out.println("   Опис: " + t.description);
            }

            if (page < totalPages) {
                System.out.print("Натисніть Enter, щоб перегорнути на наступну сторінку...");
                scanner.nextLine();
            }
        }
    }
}

