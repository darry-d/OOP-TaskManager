import java.io.Serializable;
public class Task implements Serializable {
    public String title;
    public String description;
    public String deadline;
    public String status;

public Task(String title, String description, String deadline, String status) {
    this.title = title;
    this.deadline = deadline;
    this.description = description;
    this.status = status;
}
}
