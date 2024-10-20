public class HighPriorityTask extends Task {
    public HighPriorityTask(String title, String description, String dueDate) {
        super(title, description, dueDate, "Pending", 1);
    }

    @Override
    public void execute() {
        System.out.println("High priority task: " + getTitle());
    }
}