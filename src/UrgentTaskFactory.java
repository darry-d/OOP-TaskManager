public class UrgentTaskFactory extends TaskFactory {
    @Override
    public Task createTask(String title, String description, String deadline, TaskStatus status) {
        return new UrgentTask(title, description, deadline, status);
    }
}