package com.example.CS2340FAC_Team41.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import com.example.CS2340FAC_Team41.R;

public class DiningFragment extends Fragment {
    private static final String TAG = "DiningFragment";

    private RecyclerView recyclerView;
    private DiningReservationAdapter diningAdapter;
    private ArrayList<DiningReservation> diningList;
    private Button sortButton, submitButton;
    private EditText locationInput, dateTimeInput, websiteInput, reviewsInput;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dining, container, false);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("dining").child(userId);

        recyclerView = view.findViewById(R.id.recyclerView);
        sortButton = view.findViewById(R.id.sortButton);
        submitButton = view.findViewById(R.id.submitButton);

        locationInput = view.findViewById(R.id.locationInput);
        dateTimeInput = view.findViewById(R.id.dateTimeInput);
        websiteInput = view.findViewById(R.id.websiteInput);
        reviewsInput = view.findViewById(R.id.reviewsInput);

        diningList = new ArrayList<>();
        diningAdapter = new DiningReservationAdapter(diningList);
        recyclerView.setAdapter(diningAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sortButton.setOnClickListener(v -> sortReservations());
        submitButton.setOnClickListener(v -> submitReservation());

        loadReservationsFromFirebase();

        return view;
    }

    private void loadReservationsFromFirebase() {
        // Log when we start loading data
        Log.d(TAG, "Loading reservations from Firebase...");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diningList.clear(); // Clear list to prevent duplicates on reload

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String website = dataSnapshot.child("website").getValue(String.class);
                    String reviews = dataSnapshot.child("reviews").getValue(String.class);
                    String dateString = dataSnapshot.child("date").getValue(String.class);
                    String timeString = dataSnapshot.child("time").getValue(String.class);

                    Date dateTime;
                    try {
                        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateString + " " + timeString);
                    } catch (ParseException e) {
                        Log.e(TAG, "Date parse error: " + e.getMessage());
                        continue;
                    }

                    DiningReservation reservation = new DiningReservation(location, dateTime, website, reviews);
                    diningList.add(reservation);
                    Log.d(TAG, "Added reservation: " + reservation);
                }

                Log.d(TAG, "Total reservations loaded: " + diningList.size());
                diningAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showError("Failed to load data from Firebase.");
                Log.e(TAG, "Firebase load error: " + error.getMessage());
            }
        });
    }

    private void sortReservations() {
        Collections.sort(diningList, (res1, res2) -> res2.getDateTime().compareTo(res1.getDateTime()));
        diningAdapter.notifyDataSetChanged();
    }

    private void submitReservation() {
        String location = locationInput.getText().toString().trim();
        String dateTimeStr = dateTimeInput.getText().toString().trim();
        String website = websiteInput.getText().toString().trim();
        String reviews = reviewsInput.getText().toString().trim();

        Date dateTime;
        try {
            dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateTimeStr);
        } catch (ParseException e) {
            showError("Invalid date/time format. Use yyyy-MM-dd HH:mm format.");
            return;
        }

        DiningReservation newReservation = new DiningReservation(location, dateTime, website, reviews);

        if (validateReservation(newReservation)) {
            DatabaseReference newReservationRef = databaseReference.push();
            newReservationRef.setValue(newReservation)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            diningList.add(newReservation);
                            diningAdapter.notifyDataSetChanged();
                            clearInputFields();
                            Toast.makeText(getContext(), "Reservation added.", Toast.LENGTH_SHORT).show();
                        } else {
                            showError("Failed to add reservation to Firebase.");
                        }
                    });
        }
    }


    private boolean validateReservation(DiningReservation newReservation) {
        for (DiningReservation reservation : diningList) {
            boolean sameDateTime = reservation.getDateTime().equals(newReservation.getDateTime());

            if (sameDateTime) {
                Log.d(TAG, "Duplicate found: " + reservation.getLocation() + " at " + reservation.getDateTime());
                showError("Duplicate reservation.");
                return false;
            }
        }
        return true;
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void clearInputFields() {
        locationInput.setText("");
        dateTimeInput.setText("");
        websiteInput.setText("");
        reviewsInput.setText("");
    }
}