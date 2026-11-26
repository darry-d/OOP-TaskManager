import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Repository<T extends Identifiable> implements Serializable {
    private List<T> items = new ArrayList<>();

    public void add(T item){
        items.add(item);
    }
    public void remove(T item) {
        items.remove(item);
    }

    public List<T> getAll() {
        return items;
    }
}