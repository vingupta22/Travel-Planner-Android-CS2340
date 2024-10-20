
public class TeamLead extends TeamMember implements ProjectManager {
    public TeamLead(String name, String email) {
        super(name, email);
    }

    @Override
    public void leadProject() {
        System.out.println(getName() + " is leading this project.");
    }
}