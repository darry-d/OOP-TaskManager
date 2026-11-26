import java.io.*;

public class Storage {

    public void saveProject(Project project, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(project);
            System.out.println("Дані успішно збережено у бінарний файл: " + filename);
        } catch (IOException e) {
            System.out.println("Помилка при збереженні: " + e.getMessage());
        }
    }

    public Project loadProject(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Project project = (Project) in.readObject();
            System.out.println("Дані успішно завантажено з бінарного файлу: " + filename);
            return project;
        } catch (Exception e) {
            System.out.println("Попередніх збережень не знайдено. Буде створено новий чистий проєкт.");
            return null;
        }
    }
}