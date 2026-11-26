import java.util.UUID;

public class Note  implements Identifiable {
    private String id;
    private String text;

    public Note(String text){
        this.id = UUID.randomUUID().toString();
        this.text = text;

    }
    @Override
    public String getId() {
        return id;
    }
    public String getText() {
        return text;
    }
    @Override
    public String toString() {
        return "Нотатка [ID=" + id.substring(0, 5) + "...]: " + text;
    }
}