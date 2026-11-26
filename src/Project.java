import java.io.Serializable;
import java.util.*;

public class Project implements Serializable, Iterable<Task> {
    public String name;


    public Repository<Task> taskRepository;


    public Map<String, Task> tasksById;


    public Set<String> uniqueDeadlines;


    public Queue<Task> urgentQueue;

    public Project(String name) {
        this.name = name;
        this.taskRepository = new Repository<>();
        this.tasksById = new HashMap<>();
        this.uniqueDeadlines = new HashSet<>();
        this.urgentQueue = new ArrayDeque<>();
    }


    public Project(Project original, boolean isShallow) {
        this.name = original.name + " (Поверхнева копія)";
        this.taskRepository = original.taskRepository;
        this.tasksById = original.tasksById;
        this.uniqueDeadlines = original.uniqueDeadlines;
        this.urgentQueue = original.urgentQueue;
    }


    public Project(Project original) {
        this.name = original.name + " (Глибока копія)";
        this.taskRepository = new Repository<>();
        this.tasksById = new HashMap<>();
        this.uniqueDeadlines = new HashSet<>();
        this.urgentQueue = new ArrayDeque<>();

        for (Task t : original.taskRepository.getAll()) {
            this.addTask(t.cloneTask());
        }
    }

    public void addTask(Task task) {
        this.taskRepository.add(task);
        this.tasksById.put(task.getId(), task);
        this.uniqueDeadlines.add(task.deadline);
        if (task instanceof UrgentTask) {
            this.urgentQueue.offer(task);
        }
    }

    public void removeTask(int index) throws TaskNotFoundException {
        if (index < 0 || index >= taskRepository.getAll().size()) {
            throw new TaskNotFoundException("Помилка: Завдання з номером " + (index + 1) + " не знайдено в базі!", index + 1);
        }

        Task taskToRemove = taskRepository.getAll().get(index);
        taskRepository.remove(taskToRemove);
        tasksById.remove(taskToRemove.getId());
        urgentQueue.remove(taskToRemove);


        uniqueDeadlines.clear();
        for (Task t : taskRepository.getAll()) {
            uniqueDeadlines.add(t.deadline);
        }
    }

    public Optional<Task> findTaskByTitle(String title) {
        for (Task t : taskRepository.getAll()) {
            if (t.title.equalsIgnoreCase(title)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }


    public Optional<Task> findTaskById(String id) {
        return Optional.ofNullable(tasksById.get(id));
    }

    public Optional<Task> findFirstUrgentTask() {

        return Optional.ofNullable(urgentQueue.peek());
    }


    @Override
    public Iterator<Task> iterator() {
        return new TaskIterator();
    }

    private class TaskIterator implements Iterator<Task> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < taskRepository.getAll().size();
        }

        @Override
        public Task next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Більше завдань у проєкті немає.");
            }
            return taskRepository.getAll().get(currentIndex++);
        }
    }
}