package model;
import java.lang.reflect.Array;
import java.util.*;

public class User {
    // A model for users to be implemented in a later sprint
    String firstName, lastName, age, userName;
    ArrayList<String> locations;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
        this.locations =  new ArrayList<>();
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void addLocation(String loc) {
        this.locations.add(loc);
    }


}
