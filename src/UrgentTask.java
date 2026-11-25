public class UrgentTask extends Task {
    public UrgentTask(String title, String description, String deadline, TaskStatus status) {
        super(title, description, deadline, status);
    }

    @Override
    public String getTaskType() {
        return "ТЕРМІНОВЕ!";

    }
    @Override
    public int getPriorityLevel() {
        return 10;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);

    }

    @Override
    public int hashCode() {
        return super.hashCode();

    }

    @Override
    public String toString() {
        return "[ТЕРМІНОВЕ]" + super.toString();
    }
}