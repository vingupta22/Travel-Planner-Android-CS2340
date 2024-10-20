public class RecurringTask extends Task {
    private String recurrence;

    public RecurringTask(String title, String description, String dueDate, String recurrence) {
        super(title, description, dueDate, "Pending", 2);
        this.recurrence = recurrence;
    }

    @Override
    public void execute() {
        System.out.println("Recurring task: " + getTitle() + ";" + recurrence);
    }
}