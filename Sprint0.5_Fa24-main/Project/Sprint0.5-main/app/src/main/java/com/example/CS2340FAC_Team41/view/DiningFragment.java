package com.example.CS2340FAC_Team41.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.CS2340FAC_Team41.R;
import com.example.CS2340FAC_Team41.view.DiningReservation;
import com.example.CS2340FAC_Team41.view.DiningReservationAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiningFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ArrayList<DiningReservation> diningReservations;
    private DiningReservationAdapter adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dining, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_view_dining);
        FloatingActionButton fabAddReservation = view.findViewById(R.id.fab_add_reservation);

        diningReservations = new ArrayList<>();
        adapter = new DiningReservationAdapter(getContext(), diningReservations);
        listView.setAdapter(adapter);

        // Get Firebase user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("diningReservations");

        // Fetch and display data from Firebase
        loadDiningReservations();

        // Set up FAB click listener to open the add reservation dialog
        fabAddReservation.setOnClickListener(v -> showAddReservationDialog());
    }

    private void loadDiningReservations() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diningReservations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DiningReservation reservation = snapshot.getValue(DiningReservation.class);
                    if (reservation != null) {
                        diningReservations.add(reservation);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load reservations: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddReservationDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_reservation);

        EditText locationInput = dialog.findViewById(R.id.edit_text_location);
        EditText websiteInput = dialog.findViewById(R.id.edit_text_website);
        EditText timeInput = dialog.findViewById(R.id.edit_text_time);
        EditText reviewsInput = dialog.findViewById(R.id.edit_text_reviews);
        Button saveButton = dialog.findViewById(R.id.button_save_reservation);

        saveButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString().trim();
            String website = websiteInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String reviews = reviewsInput.getText().toString().trim();

            if (!location.isEmpty() && !website.isEmpty() && !time.isEmpty()) {
                String reservationId = databaseReference.push().getKey();
                DiningReservation reservation = new DiningReservation(location, website, time, reviews);
                databaseReference.child(reservationId).setValue(reservation)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Reservation added!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
