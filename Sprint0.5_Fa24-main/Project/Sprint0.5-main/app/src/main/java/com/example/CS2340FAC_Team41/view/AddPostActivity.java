package com.example.CS2340FAC_Team41.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CS2340FAC_Team41.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;


import model.TravelPost;

public class AddPostActivity extends AppCompatActivity {

    private EditText etTripDuration, etDestinations, etNotes, etStartDate, etEndDate, etAccommodations, etDiningReservations, etRating;
    private Button btnSubmitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initialize views
        etTripDuration = findViewById(R.id.etTripDuration);
        etDestinations = findViewById(R.id.etDestinations);
        etNotes = findViewById(R.id.etNotes);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etAccommodations = findViewById(R.id.etAccommodations);
        etDiningReservations = findViewById(R.id.etDiningReservations);
        etRating = findViewById(R.id.etRating);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);

        // Set up button listener
        btnSubmitPost.setOnClickListener(v -> submitPost());
    }

    private void submitPost() {
        // Get data from input fields
        String tripDuration = etTripDuration.getText().toString().trim();
        String destinations = etDestinations.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String accommodations = etAccommodations.getText().toString().trim();
        String diningReservations = etDiningReservations.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();

        // Validate input
        if (tripDuration.isEmpty() || destinations.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Trip duration, destinations, and rating are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                Toast.makeText(this, "Rating must be between 1 and 5.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid rating value.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Push data to Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("CommunityTravelDatabase/posts");
        String postId = databaseReference.push().getKey();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TravelPost post = new TravelPost(
                postId,
                tripDuration,
                destinations,
                notes,
                startDate,
                endDate,
                accommodations,
                diningReservations,
                rating,
                userId // Include userId
        );


        databaseReference.child(postId).setValue(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add post.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
