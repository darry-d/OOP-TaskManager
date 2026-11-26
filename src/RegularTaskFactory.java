public class RegularTaskFactory extends TaskFactory {
    @Override
    public Task createTask(String title, String description, String deadline, TaskStatus status) {
        return new RegularTask(title, description, deadline, status);
    }
}