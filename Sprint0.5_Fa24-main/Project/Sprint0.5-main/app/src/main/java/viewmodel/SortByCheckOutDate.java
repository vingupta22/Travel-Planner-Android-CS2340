package viewmodel;

import java.util.List;

import model.Accommodation;

public class SortByCheckOutDate implements SortStrategy {
    @Override
    public void sort(List<Accommodation> accommodations) {
        accommodations.sort((a1, a2) -> a1.getCheckOutDate().compareTo(a2.getCheckOutDate()));
    }
}

