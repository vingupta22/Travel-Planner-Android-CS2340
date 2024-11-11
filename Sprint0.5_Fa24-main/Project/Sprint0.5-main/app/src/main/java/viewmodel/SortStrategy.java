package viewmodel;


import java.util.List;

import model.Accommodation;

public interface SortStrategy {
    /**
     * Sorts the provided list of accommodations according to a specific strategy.
     *
     * @param accommodations the list of accommodations to be sorted
     */
    void sort(List<Accommodation> accommodations);
}


