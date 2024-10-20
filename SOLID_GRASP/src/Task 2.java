public abstract class Task {
    private String title;
    private String description;
    private String dueDate;
    private String status;
    private int priority;

    public Task(String title, String description, String dueDate, String status, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public abstract void execute();
}