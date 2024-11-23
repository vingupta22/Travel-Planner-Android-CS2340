package com.example.CS2340FAC_Team41.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CS2340FAC_Team41.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.TravelPost;

public class AddPostActivity extends AppCompatActivity {

    private EditText etTripDuration, etDestinations, etNotes;
    private Button btnSubmitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        etTripDuration = findViewById(R.id.etTripDuration);
        etDestinations = findViewById(R.id.etDestinations);
        etNotes = findViewById(R.id.etNotes);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);

        btnSubmitPost.setOnClickListener(v -> {
            submitPost();
        });
    }

    private void submitPost() {
        String duration = etTripDuration.getText().toString();
        String destinations = etDestinations.getText().toString();
        String notes = etNotes.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("CommunityTravelDatabase/posts");

        String postId = databaseReference.push().getKey();
        TravelPost post = new TravelPost(postId, duration, destinations, notes);

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

