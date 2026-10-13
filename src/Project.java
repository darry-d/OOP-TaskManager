import java.io.Serializable;
import java.util.ArrayList;



public class Project implements Serializable {
    public String name;
    public ArrayList<Task> tasks;

    public Project(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public Project(Project original, boolean isSahllow) {
        this.name = original.name + "(Поверхнева копія)";
        this.tasks = original.tasks;
    }

    public Project(Project original) {
        this.name = original.name + "(Глибока копія)";
        this.tasks = new ArrayList<>(original.tasks);

    }
    public void addTask(Task task) {

        this.tasks.add(task);
    }
}
