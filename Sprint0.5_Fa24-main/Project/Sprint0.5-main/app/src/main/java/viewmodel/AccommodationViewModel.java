package viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.Accommodation;

public class AccommodationViewModel extends ViewModel {
    private MutableLiveData<List<Accommodation>> accommodationsLiveData = new MutableLiveData<>(new ArrayList<>());
    private DatabaseReference databaseRef;
    private SortStrategy sortStrategy;

    public AccommodationViewModel() {
        // Link to user-specific accommodations in Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("accommodations");

        // Fetch initial data if needed
        fetchAccommodations();
    }

    public void addAccommodation(Accommodation accommodation) {
        String key = databaseRef.push().getKey();
        databaseRef.child(key).setValue(accommodation);
    }

    private void fetchAccommodations() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Accommodation> accommodations = new ArrayList<>();
                for (DataSnapshot accommodationSnapshot : snapshot.getChildren()) {
                    Accommodation accommodation = accommodationSnapshot.getValue(Accommodation.class);
                    accommodations.add(accommodation);
                }
                accommodationsLiveData.setValue(accommodations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or handle database fetch error here
            }
        });
    }

    public void setSortStrategy(SortStrategy strategy) {
        this.sortStrategy = strategy;
        applySort();
    }

    private void applySort() {
        List<Accommodation> accommodations = accommodationsLiveData.getValue();
        if (sortStrategy != null && accommodations != null) {
            sortStrategy.sort(accommodations);
            accommodationsLiveData.setValue(accommodations);
        }
    }

    public LiveData<List<Accommodation>> getAccommodationsLiveData() {
        return accommodationsLiveData;
    }
}
