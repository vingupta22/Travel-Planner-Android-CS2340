package model;


import java.util.ArrayList;

public class User {
    // A model for users to be implemented in a later sprint
    private String firstName;
    private String lastName;
    private String age;
    private String userName;
    private ArrayList<String> locations;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor for userName and locations
     * @param userName the userName name
     */
    public User(String userName) {
        this.userName = userName;
        this.locations =  new ArrayList<>();
    }

    /**
     * Gets the locations
     * @return the locations you requested
     */
    public ArrayList<String> getLocations() {
        return locations;
    }

    /**
     * Adds the location
     * @param loc the location you want to add
     */
    public void addLocation(String loc) {
        this.locations.add(loc);
    }


}
