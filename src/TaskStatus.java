public enum TaskStatus {
    TODO("До виконання"),
    IN_PROGRESS("В процесі"),
    DONE("Виконано");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;

    }

    @Override
    public String toString() {
        return displayName;
    }
}
