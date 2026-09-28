import java.io.*;

public class Storage {

public void saveProject(Project project, String filename) {
    try {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(project);

        out.close();
        fileOut.close();
        System.out.println("Дані успішно збережено у файл: " + filename);

    } catch (Exception e) {
        System.out.println("Помилка при збереженні: " + e.getMessage());

    }
}

public Project loadProject(String filename) {
    try {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        Project project = (Project) in.readObject();

        in.close();
        fileIn.close();
        System.out.println("Дані успішно завантажено з файлу: " + filename);
        return project;

    } catch (Exception e) {
        System.out.println("Попередніх збережень не знайдено. Буде створено новий чистий проєкт.");
        return null;
    }
}
}