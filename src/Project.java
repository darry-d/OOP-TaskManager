import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
public class Project implements Serializable {
    public String name;

    // Замість ArrayList тепер використовуємо наш Generic Repository
    public Repository<Task> taskRepository;

    public Project(String name) {
        this.name = name;
        this.taskRepository = new Repository<>();
    }


    public Project(Project original, boolean isShallow) {
        this.name = original.name + " (Поверхнева копія)";
        this.taskRepository = original.taskRepository;
    }


    public Project(Project original) {
        this.name = original.name + " (Глибока копія)";
        this.taskRepository = new Repository<>();

        for (Task t : original.taskRepository.getAll()) {
            this.taskRepository.add(t);
        }
    }

    public void addTask(Task task) {
        this.taskRepository.add(task);
    }

    public void removeTask(int index) throws TaskNotFoundException {

        if (index < 0 || index >= taskRepository.getAll().size()) {
            throw new TaskNotFoundException("Помилка: Завдання з номером " + (index + 1) + " не знайдено в базі!", index + 1);
        }


        Task taskToRemove = taskRepository.getAll().get(index);
        taskRepository.remove(taskToRemove);
    }
    public Optional<Task> findTaskByTitle(String title) {
        for (Task t: taskRepository.getAll())  {
            if (t.title.equalsIgnoreCase(title)){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }
    public Optional<Task> findTaskById(String id) {
        for (Task t : taskRepository.getAll()) {
            if (t.getId().equals(id)){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }
    public Optional<Task> findFirstUrgentTask() {
        for (Task t : taskRepository.getAll()) {
            if ( t instanceof UrgentTask) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }
}