package viewmodel;

import java.util.List;

import model.Accommodation;

public class SortByCheckInDate implements SortStrategy {
    @Override
    public void sort(List<Accommodation> accommodations) {
        accommodations.sort((a1, a2) -> a1.getCheckInDate().compareTo(a2.getCheckInDate()));
    }
}

