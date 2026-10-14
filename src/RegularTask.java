public class RegularTask extends Task {
    public RegularTask(String title, String description, String deadline, TaskStatus status) {
        super(title, description, deadline, status);
    }

    @Override
    public String getTaskType() {
        return "Звичайне";

    }

    @Override
    public int getPriorityLevel() {
        return 1;
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
        return "[Звичайне]" + super.toString();
    }
}