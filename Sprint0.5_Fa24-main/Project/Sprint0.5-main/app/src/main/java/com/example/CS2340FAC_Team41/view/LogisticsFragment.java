// File: app/src/main/java/com/example/CS2340FAC_Team41/view/LogisticsFragment.java
package com.example.CS2340FAC_Team41.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CS2340FAC_Team41.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

import model.Note;

// Assuming you have a Note model already defined in your project


// If you don't have a NotesAdapter, see the NotesAdapter inner class below
// Otherwise, remove the inner NotesAdapter class

public class LogisticsFragment extends Fragment {

    private PieChart pieChart;
    private TextView tvTotalDays;
    private Button btnVisualize;
    private Button btnAddNote;
    private Button btnInvite;
    private RecyclerView recyclerNotes;
    private NotesAdapter notesAdapter;
    private DatabaseReference notesRef;
    private List<Note> noteList = new ArrayList<>();
    private DatabaseReference travelLogsRef;
    private DatabaseReference vacationLogsRef;
    private String userId;
    private int allottedDays = 0;
    private int totalPlannedDays = 0;

    // Contributors
    private RecyclerView recyclerContributors;
    private ContributorsAdapter contributorsAdapter;
    private List<Contributor> contributorList = new ArrayList<>();
    private DatabaseReference contributorsRef;

    // Firebase Auth
    private FirebaseAuth mAuth;

    // Assume a single trip for simplicity; replace with actual trip ID logic as needed
    private String currentTripId = "current_trip_id"; // TODO: Replace with actual trip ID retrieval

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_logistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        userId = mAuth.getCurrentUser().getUid();

        // TODO: Replace with actual trip ID retrieval logic
        // For example, you might pass the trip ID as an argument to the fragment
        // Bundle args = getArguments();
        // if (args != null) {
        //     currentTripId = args.getString("tripId");
        // } else {
        //     Toast.makeText(getContext(), "Trip ID not provided", Toast.LENGTH_SHORT).show();
        //     return;
        // }

        // Initialize Firebase References
        travelLogsRef = FirebaseDatabase.getInstance()
                .getReference("trips")
                .child(currentTripId)
                .child("travelLogs");

        vacationLogsRef = FirebaseDatabase.getInstance()
                .getReference("trips")
                .child(currentTripId)
                .child("vacationLogs");

        notesRef = FirebaseDatabase.getInstance().getReference("trips")
                .child(currentTripId)
                .child("notes");

        contributorsRef = FirebaseDatabase.getInstance().getReference("trips")
                .child(currentTripId)
                .child("contributors");

        // Initialize UI Components
        btnAddNote = view.findViewById(R.id.btn_add_note);
        btnInvite = view.findViewById(R.id.btn_invite);
        recyclerNotes = view.findViewById(R.id.recycler_notes);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NotesAdapter(noteList);
        recyclerNotes.setAdapter(notesAdapter);

        pieChart = view.findViewById(R.id.pie_chart);
        tvTotalDays = view.findViewById(R.id.tv_total_days);
        btnVisualize = view.findViewById(R.id.btn_visualize);

        // Initialize Contributors RecyclerView
        recyclerContributors = view.findViewById(R.id.recycler_contributors);
        recyclerContributors.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        contributorsAdapter = new ContributorsAdapter(contributorList);
        recyclerContributors.setAdapter(contributorsAdapter);

        // Load existing notes and contributors
        loadNotes();
        loadContributors();

        // Add Note Button Click Listener
        btnAddNote.setOnClickListener(v -> showAddNoteDialog());

        // Invite Button Click Listener
        btnInvite.setOnClickListener(v -> showInviteDialog());

        // Set up button listener for the pie chart visualization
        btnVisualize.setOnClickListener(v -> visualizeTripDays());

        // Load allotted and planned vacation days
        loadAllottedDays();
        loadPlannedVacationDays();
    }

    /**
     * Load the list of notes from Firebase
     */
    private void loadNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteList.clear();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    if (note != null) {
                        noteList.add(note);
                    }
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load notes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Show a dialog to add a new note
     */
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Note");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String noteContent = input.getText().toString().trim();
            if (!noteContent.isEmpty()) {
                addNoteToFirebase(noteContent);
            } else {
                Toast.makeText(getContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Add a new note to Firebase
     *
     * @param content The content of the note
     */
    private void addNoteToFirebase(String content) {
        String noteId = notesRef.push().getKey();
        if (noteId == null) {
            Toast.makeText(getContext(), "Failed to generate note ID", Toast.LENGTH_SHORT).show();
            return;
        }
        Note newNote = new Note(userId, content, System.currentTimeMillis());
        notesRef.child(noteId).setValue(newNote).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Load the allotted vacation days from Firebase
     */
    private void loadAllottedDays() {
        vacationLogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allottedDays = 0; // Reset before recalculating
                for (DataSnapshot log : snapshot.getChildren()) {
                    String durationStr = log.child("duration").getValue(String.class);
                    if (durationStr != null && !durationStr.isEmpty()) {
                        try {
                            allottedDays += Integer.parseInt(durationStr);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid duration format", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                updateTotalDaysText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load allotted days", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Load the planned vacation days from Firebase
     */
    private void loadPlannedVacationDays() {
        travelLogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalPlannedDays = 0; // Reset before recalculating

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                for (DataSnapshot trip : snapshot.getChildren()) {
                    String startDateStr = trip.child("startTime").getValue(String.class);
                    String endDateStr = trip.child("endTime").getValue(String.class);

                    if (startDateStr == null || endDateStr == null) {
                        continue;
                    }

                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);

                        if (startDate == null || endDate == null) {
                            continue;
                        }

                        long difference = endDate.getTime() - startDate.getTime();
                        int tripDays = (int) (difference / (1000 * 60 * 60 * 24)) + 1;

                        totalPlannedDays += tripDays;

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing trip dates", Toast.LENGTH_SHORT).show();
                    }
                }
                updateTotalDaysText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load travel logs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Update the Total Days TextView
     */
    private void updateTotalDaysText() {
        tvTotalDays.setText("Planned Vacation Days: " + totalPlannedDays);
    }

    /**
     * Visualizes the trip days using a PieChart
     */
    private void visualizeTripDays() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(totalPlannedDays, "Planned Days"));
        entries.add(new PieEntry(Math.max(allottedDays - totalPlannedDays, 0), "Remaining Days"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.orange));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(getResources().getColor(R.color.white));

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f%%", value);
            }
        });

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Trip Overview");
        pieChart.setCenterTextSize(18f);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.getDescription().setEnabled(false);

        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12f);
        legend.setFormSize(10f);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        pieChart.invalidate();
    }

    /**
     * Load the list of contributors from Firebase
     */
    private void loadContributors() {
        contributorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contributorList.clear();
                for (DataSnapshot contributorSnapshot : snapshot.getChildren()) {
                    Contributor contributor = contributorSnapshot.getValue(Contributor.class);
                    if (contributor != null) {
                        contributorList.add(contributor);
                    }
                }
                contributorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load contributors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Show a dialog to invite a new user by email
     */
    private void showInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Invite User");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setHint("Enter user's email");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = input.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                inviteUserByEmail(email);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Invite a user by their email address
     *
     * @param email The email of the user to invite
     */
    private void inviteUserByEmail(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Convert email to lowercase for a consistent query, assuming emails are stored in lowercase
        String queryEmail = email.toLowerCase();

        usersRef.orderByChild("email").equalTo(queryEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean userFound = false;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String invitedUserId = userSnapshot.getKey();
                        String invitedUserEmail = userSnapshot.child("email").getValue(String.class);

                        if (invitedUserId != null && invitedUserEmail != null) {
                            userFound = true;
                            // Check if the user is already a contributor
                            contributorsRef.child(invitedUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot contributorSnapshot) {
                                    if (contributorSnapshot.exists()) {
                                        Toast.makeText(getContext(), "User is already a contributor", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Add the user as a contributor
                                        Contributor newContributor = new Contributor(invitedUserId, invitedUserEmail);
                                        contributorsRef.child(invitedUserId).setValue(newContributor)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "User invited successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Failed to invite user", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Error checking contributor status", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    if (!userFound) {
                        Toast.makeText(getContext(), "No user found with that email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No user found with that email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error searching for user", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Contributor model class
     */
    private static class Contributor {
        private String userId;
        private String email;

        // Default constructor required for Firebase
        public Contributor() {}

        public Contributor(String userId, String email) {
            this.userId = userId;
            this.email = email;
        }

        // Getters and setters
        public String getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * Adapter for Contributors RecyclerView
     */
    private class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ContributorViewHolder> {

        private List<Contributor> contributorsList;

        public ContributorsAdapter(List<Contributor> contributorsList) {
            this.contributorsList = contributorsList;
        }

        @NonNull
        @Override
        public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Use Android's built-in simple_list_item_1 layout
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ContributorViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ContributorViewHolder holder, int position) {
            Contributor contributor = contributorsList.get(position);
            holder.tvContributorEmail.setText(contributor.getEmail());
        }

        @Override
        public int getItemCount() {
            return contributorsList.size();
        }

        class ContributorViewHolder extends RecyclerView.ViewHolder {
            TextView tvContributorEmail;

            public ContributorViewHolder(@NonNull View itemView) {
                super(itemView);
                tvContributorEmail = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    /**
     * Basic NotesAdapter implementation
     * Remove this if you already have a NotesAdapter in your project
     */
    private class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

        private List<Note> notesList;

        public NotesAdapter(List<Note> notesList) {
            this.notesList = notesList;
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Use Android's built-in simple_list_item_2 layout
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new NoteViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            Note note = notesList.get(position);
            holder.tvNoteContent.setText(note.getContent());
            // Format timestamp to a readable date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date(note.getTimestamp()));
            holder.tvNoteDate.setText(date);
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }

        class NoteViewHolder extends RecyclerView.ViewHolder {
            TextView tvNoteContent;
            TextView tvNoteDate;

            public NoteViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNoteContent = itemView.findViewById(android.R.id.text1);
                tvNoteDate = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
