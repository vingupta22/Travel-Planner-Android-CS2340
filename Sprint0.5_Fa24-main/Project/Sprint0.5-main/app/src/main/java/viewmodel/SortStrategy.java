package viewmodel;


import java.util.List;

import model.Accommodation;

public interface SortStrategy {
    void sort(List<Accommodation> accommodations);
}


