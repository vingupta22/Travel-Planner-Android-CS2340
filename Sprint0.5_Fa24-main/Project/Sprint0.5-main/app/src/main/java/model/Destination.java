package model;
public class Destination {
    // The single instance of the class
    private static Destination instance;

    // Private constructor to prevent instantiation from other classes
    private Destination() {
    }

    // Public static method to provide access to the single instance
    public static synchronized Destination getInstance() {
        if (instance == null) {
            instance = new Destination();
        }
        return instance;
    }

    private String name;
    private int daysPlanned;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDaysPlanned() {
        return daysPlanned;
    }

    public void setDaysPlanned(int daysPlanned) {
        this.daysPlanned = daysPlanned;
    }
}