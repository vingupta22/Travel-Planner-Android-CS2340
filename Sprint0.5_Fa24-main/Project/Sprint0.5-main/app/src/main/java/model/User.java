package model;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String age;
    private String email; // Added email field
    private ArrayList<String> locations;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with email and locations
     * @param email the email of the user
     */
    public User(String email) {
        this.email = email;
        this.locations = new ArrayList<>();
    }

    /**
     * Getters and Setters
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age){
        this.age = age;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void addLocation(String loc) {
        this.locations.add(loc);
    }
}
