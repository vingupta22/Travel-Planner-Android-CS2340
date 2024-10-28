package model;
public class Destination {
    // The single instance of the class
    private static Destination instance;

    /**
     * Private constructor to prevent instantiation from other classes
     */
    private Destination() {
    }

    /**
     * Public static method to provide access to the single instance
     * @return the instance
     */
    public static synchronized Destination getInstance() {
        if (instance == null) {
            instance = new Destination();
        }
        return instance;
    }

    private String name;
    private int daysPlanned;

    /**
     * Gets the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the number of days planned
     * @return days planned
     */
    public int getDaysPlanned() {
        return daysPlanned;
    }

    /**
     * Sets the number of days planned
     * @param daysPlanned number of days planned
     */
    public void setDaysPlanned(int daysPlanned) {
        this.daysPlanned = daysPlanned;
    }
}
