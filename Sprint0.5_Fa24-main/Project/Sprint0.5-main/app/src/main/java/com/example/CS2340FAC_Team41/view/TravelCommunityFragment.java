package com.example.CS2340FAC_Team41.view;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        EditText etDuration = dialogView.findViewById(R.id.etTripDuration);
        EditText etDestinations = dialogView.findViewById(R.id.etDestinations);
        EditText etNotes = dialogView.findViewById(R.id.etNotes);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmitPost);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setTitle("Add Travel Post");

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();

        btnSubmit.setOnClickListener(v -> {
            String duration = etDuration.getText().toString().trim();
            String destinations = etDestinations.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (duration.isEmpty() || destinations.isEmpty()) {
                Toast.makeText(getContext(), "Duration and Destinations are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            TravelPost newPost = new TravelPost(
                    databaseReference.push().getKey(),
                    duration,
                    destinations,
                    notes
            );

            databaseReference.child(newPost.getPostId()).setValue(newPost)
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
}

