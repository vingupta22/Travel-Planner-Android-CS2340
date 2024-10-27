package viewmodel;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class DestinationViewModel extends ViewModel {

    // A list to simulate travel destinations
    private List<String> destinations = new ArrayList<>();

    // Add a destination to the list
    public void addDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            destinations.add(destination);
        }
    }

    // Retrieve the list of destinations
    public List<String> getDestinations() {
        return destinations;
    }

    // A method to validate if a location input is valid
    public boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    // A method to clear all destinations
    public void clearDestinations() {
        destinations.clear();
    }
}