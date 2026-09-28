import java.io.Serializable;
import java.util.ArrayList;



public class Project implements Serializable {
    public String name;
    public ArrayList<Task> tasks;

    public Project(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }
    public void addTask(Task newTask) {
        tasks.add(newTask);
    }
}
