import java.io.Serializable;
import java.util.Objects;
public  abstract class Task implements Serializable {
    public String title;
    public String description;
    public String deadline;
    public TaskStatus status;

    public Task(String title, String description, String deadline, TaskStatus status) {
    this.title = title;
    this.deadline = deadline;
    this.description = description;
    this.status = status;
}
public abstract String getTaskType();
    public abstract int getPriorityLevel();

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(deadline, task.deadline);

    }

    @Override
    public int hashCode(){
        return Objects.hash(title, deadline);

    }
    @Override
    public String toString() {
        return "Завдання: '" + title + "', Дедлайн: " + deadline;

    }
}
