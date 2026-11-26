public abstract class TaskFactory {
    public abstract Task createTask(String title, String description, String deadline, TaskStatus status);

    public Task createAndAdd(Project project, String title, String description, String deadline) {
        Task task = createTask(title, description, deadline, TaskStatus.TODO);
        project.addTask(task);
        ActionLogger.getInstance().log("Створено через фабрику: " + task.title);
        return task;
    }
}