package com.example.CS2340FAC_Team41.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.CS2340FAC_Team41.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.TravelPost;

public class TravelCommunityFragment extends Fragment {
    private static final String TAG = "TravelCommunityFragment";

    private RecyclerView recyclerView;
    private TravelPostAdapter postAdapter;
    private ArrayList<TravelPost> postList;
    private FloatingActionButton fabAddPost;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_travel, container, false);
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("CommunityTravelDatabase/posts");

        recyclerView = view.findViewById(R.id.recyclerTravelPosts);
        fabAddPost = view.findViewById(R.id.fab_add_post);

        postList = new ArrayList<>();
        postAdapter = new TravelPostAdapter(postList);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddPost.setOnClickListener(v -> showAddPostDialog());

        loadPostsFromFirebase();
        return view;
    }

    private void showAddPostDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_post, null);

        // Initialize dialog fields
        EditText etTripDuration = dialogView.findViewById(R.id.etTripDuration);
        EditText etDestinations = dialogView.findViewById(R.id.etDestinations);
        EditText etNotes = dialogView.findViewById(R.id.etNotes);
        EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        EditText etEndDate = dialogView.findViewById(R.id.etEndDate);
        EditText etAccommodations = dialogView.findViewById(R.id.etAccommodations);
        EditText etDiningReservations = dialogView.findViewById(R.id.etDiningReservations);
        EditText etRating = dialogView.findViewById(R.id.etRating);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmitPost);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setTitle("Add Travel Post");

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();

        btnSubmit.setOnClickListener(v -> {
            // Get data from dialog fields
            String tripDuration = etTripDuration.getText().toString().trim();
            String destinations = etDestinations.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String accommodations = etAccommodations.getText().toString().trim();
            String diningReservations = etDiningReservations.getText().toString().trim();
            String ratingStr = etRating.getText().toString().trim();

            if (tripDuration.isEmpty() || destinations.isEmpty() || ratingStr.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            int rating;
            try {
                rating = Integer.parseInt(ratingStr);
                if (rating < 1 || rating > 5) {
                    Toast.makeText(getContext(), "Rating must be between 1 and 5.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid rating value.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);

                if (start != null && end != null && start.after(end)) {
                    Toast.makeText(getContext(), "Start date cannot be after end date.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the current user's ID
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Create new post
            String postId = databaseReference.push().getKey();
            TravelPost newPost = new TravelPost(
                    postId,
                    tripDuration,
                    destinations,
                    notes,
                    startDate,
                    endDate,
                    accommodations,
                    diningReservations,
                    rating,
                    userId
            );


            // Save to Firebase
            databaseReference.child(postId).setValue(newPost)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            postList.add(newPost);
                            postAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Travel Post added.", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Failed to add Travel Post.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        alertDialog.show();
    }

    private void loadPostsFromFirebase() {
        Log.d(TAG, "Loading travel posts from Firebase...");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TravelPost post = postSnapshot.getValue(TravelPost.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Firebase load error: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<TravelPost> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<TravelPost> newList) {
        postList = newList;
    }
}
