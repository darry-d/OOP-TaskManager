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
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();


            switch (choice) {
                case "1":
                    showTasksPaginated(myProject, scanner);
                    break;
                case "2":
                    System.out.println("Назва: ");
                    String title = scanner.nextLine();
                    System.out.print("Опис: ");
                    String desc = scanner.nextLine();

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
                    myProject.addTask(new Task(title, desc, deadline, "TODO"));
                    System.out.println("Завдання додано!");
                    break;
                case "3":
                    System.out.print("Введіть номер завдання: ");
                    try {
                        int updateIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (updateIndex >= 0 && updateIndex < myProject.tasks.size()) {
                            myProject.tasks.get(updateIndex).status = "DONE";
                            System.out.println("Статус оновлено на ВИКОНАНО!");
                        } else {
                            System.out.println("Невірний номер.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Будь ласка, введіть число.");
                    }
                    break;
                case "4":
                    System.out.println("Введіть номер завдання для видалення: ");
                    int deleteIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if (deleteIndex >= 0 && deleteIndex < myProject.tasks.size()) {
                        myProject.tasks.remove(deleteIndex);
                        System.out.println("Завдання видалено!");
                    } else {
                        System.out.println("Невірний номер.");
                    }
                    break;

            case "5":
                storage.saveProject(myProject, filename);
                isRunning = false;
                break;
            default:
                System.out.println("Невідома команда. Спробуйте ще раз.");

            }

        }
        scanner.close();
    }

    public static void showTasksPaginated(Project project,Scanner scanner) {
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
                System.out.println((i + 1) + ". [" + t.status + "] " + t.title + " (Дедлайн: " + t.deadline + ")");
                System.out.println("   Опис: " + t.description);
            }

            if (page < totalPages) {
                System.out.print("Натисніть Enter, щоб перегорнути на наступну сторінку...");
                scanner.nextLine();
            }
        }
    }
}

