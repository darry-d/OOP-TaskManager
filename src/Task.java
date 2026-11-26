import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public abstract class Task implements Identifiable, Serializable, Comparable<Task> {
    public String id;
    public String title;
    public String description;
    public String deadline;
    public TaskStatus status;

    public Task(String title, String description, String deadline, TaskStatus status) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    public abstract String getTaskType();
    public abstract int getPriorityLevel();

    public LocalDate getDeadlineDate() {
        try {
            return LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            return LocalDate.MAX;
        }
    }

    @Override
    public int compareTo(Task other) {
        int dateCmp = this.getDeadlineDate().compareTo(other.getDeadlineDate());
        if (dateCmp != 0) {
            return dateCmp;
        }
        return this.title.compareToIgnoreCase(other.title);
    }

    public static Comparator<Task> BY_TITLE = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.title.compareToIgnoreCase(t2.title);
        }
    };

    public static Comparator<Task> BY_PRIORITY = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return Integer.compare(t2.getPriorityLevel(), t1.getPriorityLevel());
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(deadline, task.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, deadline);
    }

    @Override
    public String toString() {
        return "Завдання: '" + title + "', Дедлайн: " + deadline;
    }
}