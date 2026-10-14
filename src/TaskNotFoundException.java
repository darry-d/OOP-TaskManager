public class TaskNotFoundException extends TaskManagerException {
    private final int taskId;

    public TaskNotFoundException(String message, int taskId) {
        super(message);
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }
}