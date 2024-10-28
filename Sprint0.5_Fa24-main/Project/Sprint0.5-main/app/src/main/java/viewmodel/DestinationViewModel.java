package viewmodel;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class DestinationViewModel extends ViewModel {

    // A list to simulate travel destinations
    private List<String> destinations = new ArrayList<>();

    /**
     * Add a destination to the list
     * @param destination the destination wanted
     */
    public void addDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            destinations.add(destination);
        }
    }

    /**
     * Retrieve the list of destinations
     * @return the destination
     */
    public List<String> getDestinations() {
        return destinations;
    }

    /**
     * A method to validate if a location input is valid
     * @return whether its a valid location or not
     * @param location the location you want to check
      */
    public boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    /**
     * A method to clear the destinations
     */
    public void clearDestinations() {
        destinations.clear();
    }
}

