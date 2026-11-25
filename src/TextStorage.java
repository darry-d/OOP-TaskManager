import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextStorage {

    // Збереження проєкту та завдань у текстовий файл через BufferedWriter
    public void saveProjectToText(Project project, String filename) {
        Path path = Paths.get(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            // Сутність 1: Project
            writer.write("PROJECT;" + sanitize(project.name));
            writer.newLine();

            // Сутність 2: Task (RegularTask та UrgentTask)
            for (Task t : project.taskRepository.getAll()) {
                String type = (t instanceof UrgentTask) ? "URGENT" : "REGULAR";
                String line = String.join(";",
                        type,
                        sanitize(t.id),
                        sanitize(t.title),
                        sanitize(t.description),
                        sanitize(t.deadline),
                        t.status.name()
                );
                writer.write(line);
                writer.newLine();
            }

            writer.flush();
            System.out.println("Проєкт збережено у текстовий файл: " + filename + " (" + Files.size(path) + " байт)");
        } catch (IOException e) {
            System.out.println("Помилка збереження у текстовий файл: " + e.getMessage());
        }
    }

    // Завантаження проєкту та завдань із текстового файлу через BufferedReader
    public Project loadProjectFromText(String filename) {
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            System.out.println("Текстовий файл бази даних не знайдено: " + filename);
            return null;
        }

        Project loadedProject = null;

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                // Відновлюємо сутність Project
                if (parts[0].equals("PROJECT") && parts.length >= 2) {
                    loadedProject = new Project(parts[1]);
                }
                // Відновлюємо сутності RegularTask / UrgentTask
                else if ((parts[0].equals("REGULAR") || parts[0].equals("URGENT")) && parts.length == 6) {
                    if (loadedProject == null) {
                        loadedProject = new Project("Головний проєкт");
                    }

                    String type = parts[0];
                    String id = parts[1];
                    String title = parts[2];
                    String description = parts[3];
                    String deadline = parts[4];
                    TaskStatus status = TaskStatus.valueOf(parts[5]);

                    Task task = type.equals("URGENT")
                            ? new UrgentTask(title, description, deadline, status)
                            : new RegularTask(title, description, deadline, status);

                    task.id = id; // Відновлюємо оригінальний ID
                    loadedProject.addTask(task);
                }
            }

            if (loadedProject != null) {
                System.out.println("Дані успішно завантажено з текстового файлу: " + filename);
            }
            return loadedProject;
        } catch (Exception e) {
            System.out.println("Помилка читання текстового файлу: " + e.getMessage());
            return null;
        }
    }

    // Захист від зайвих ';' у тексті, щоб не зламати формат рядка
    private String sanitize(String value) {
        return value == null ? "" : value.replace(";", ",");
    }
}