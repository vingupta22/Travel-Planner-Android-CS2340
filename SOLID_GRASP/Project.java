import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<Task> tasks;
    private List<TeamMember> teamMembers;

    public Project(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void addTeamMember(TeamMember member) {
        teamMembers.add(member);
    }

    public void removeTeamMember(TeamMember member) {
        teamMembers.remove(member);
    }

    public void executeAllTasks() {
        for (Task task : tasks) {
            task.execute();
        }
    }
}