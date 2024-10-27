package model;

public class Destination {
    // The single instance of the class
    private static Destination instance;

    // Private constructor to prevent instantiation from other classes
    private Destination() {
        // Initialization logic if required
    }

    // Public static method to provide access to the single instance
    public static synchronized Destination getInstance() {
        if (instance == null) {
            instance = new Destination();
        }
        return instance;
    }

    // Example fields and methods (you can extend this as needed)
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
