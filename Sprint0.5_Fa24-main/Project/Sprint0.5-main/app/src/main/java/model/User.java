package model;

import java.util.ArrayList;

/**
 * Represents a user with personal information and a list of favorite locations.
 */
public class User {
    private String firstName;
    private String lastName;
    private String age;
    private String email;
    private ArrayList<String> locations;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructs a User with the specified email and an empty list of locations.
     *
     * @param email the email of the user
     */
    public User(String email) {
        this.email = email;
        this.locations = new ArrayList<>();
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name as a String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name as a String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the age of the user.
     *
     * @return the age as a String
     */
    public String getAge() {
        return age;
    }

    /**
     * Sets the age of the user.
     *
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the list of favorite locations of the user.
     *
     * @return the list of locations as an ArrayList
     */
    public ArrayList<String> getLocations() {
        return locations;
    }

    /**
     * Adds a location to the user's list of favorite locations.
     *
     * @param loc the location to add
     */
    public void addLocation(String loc) {
        this.locations.add(loc);
    }
}
