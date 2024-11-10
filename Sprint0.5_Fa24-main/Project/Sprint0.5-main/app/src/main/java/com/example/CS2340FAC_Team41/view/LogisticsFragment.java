package com.example.CS2340FAC_Team41.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Contributor;
import model.ContributorsAdapter;
import model.Note;
import model.NotesAdapter;

public class LogisticsFragment extends Fragment {

    private PieChart pieChart;
    private TextView tvTotalDays;
    private Button btnVisualize;
    private Button btnAddNote;
    private Button btnInvite;
    private RecyclerView recyclerNotes;
    private RecyclerView recyclerContributors;
    private RecyclerView recyclerSharedNotes; // New RecyclerView for Shared Notes
    private NotesAdapter notesAdapter;
    private ContributorsAdapter contributorsAdapter;
    private NotesAdapter sharedNotesAdapter; // New Adapter for Shared Notes
    private DatabaseReference notesRef;
    private DatabaseReference travelLogsRef;
    private DatabaseReference vacationLogsRef;
    private DatabaseReference contributorsRef;
    private DatabaseReference sharedWithRef;
    private List<Note> noteList = new ArrayList<>();
    private List<Note> sharedNoteList = new ArrayList<>(); // New List for Shared Notes
    private List<Contributor> contributorList = new ArrayList<>();
    private List<String> sharedWithList = new ArrayList<>();
    private String userId;
    private int allottedDays = 0;
    private int totalPlannedDays = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_logistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase References
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        travelLogsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("travelLogs");
        vacationLogsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("vacationLogs");

        notesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("notes");

        contributorsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("contributors");

        // Reference to sharedWith list
        sharedWithRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("sharedWith");

        // Initialize UI Components
        btnAddNote = view.findViewById(R.id.btn_add_note);
        recyclerNotes = view.findViewById(R.id.recycler_notes);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NotesAdapter(noteList);
        recyclerNotes.setAdapter(notesAdapter);

        // Initialize Shared Notes RecyclerView
        recyclerSharedNotes = view.findViewById(R.id.recycler_shared_notes);
        recyclerSharedNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedNotesAdapter = new NotesAdapter(sharedNoteList);
        recyclerSharedNotes.setAdapter(sharedNotesAdapter);

        pieChart = view.findViewById(R.id.pie_chart);
        tvTotalDays = view.findViewById(R.id.tv_total_days);
        btnVisualize = view.findViewById(R.id.btn_visualize);

        // Initialize Contributors UI
        btnInvite = view.findViewById(R.id.btn_invite);
        recyclerContributors = view.findViewById(R.id.recycler_contributors);
        recyclerContributors.setLayoutManager(new LinearLayoutManager(getContext()));
        contributorsAdapter = new ContributorsAdapter(getContext(), contributorList);
        recyclerContributors.setAdapter(contributorsAdapter);

        // Load existing notes and contributors
        loadNotes();
        loadContributors();

        // Load sharedWith list and then load shared data
        loadSharedWithAndSharedData();

        // Set Click Listeners
        btnAddNote.setOnClickListener(v -> showAddNoteDialog());
        btnInvite.setOnClickListener(v -> showInviteUserDialog());

        // Load allotted and planned vacation days
        loadAllottedDays();
        loadPlannedVacationDays();

        // Set up button listener for the pie chart visualization
        btnVisualize.setOnClickListener(v -> visualizeTripDays());
    }

    private void loadNotes(){
        notesRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                noteList.clear();
                for(DataSnapshot noteSnapshot : snapshot.getChildren()){
                    Note note = noteSnapshot.getValue(Note.class);
                    if(note != null){
                        noteList.add(note);
                    }
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(getContext(), "Failed to load notes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddNoteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Note");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String noteContent = input.getText().toString().trim();
            if(!noteContent.isEmpty()){
                addNoteToFirebase(noteContent);
            } else {
                Toast.makeText(getContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addNoteToFirebase(String content){
        String noteId = notesRef.push().getKey();
        if(noteId != null){
            Note newNote = new Note(userId, content, System.currentTimeMillis());
            notesRef.child(noteId).setValue(newNote).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add note", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Failed to generate note ID", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads the allotted days from vacation logs
     */
    private void loadAllottedDays(){
        vacationLogsRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot log : snapshot.getChildren()){
                    String durationStr = log.child("duration").getValue(String.class);
                    if(durationStr != null && !durationStr.isEmpty()){
                        try {
                            allottedDays += Integer.parseInt(durationStr);
                        } catch(NumberFormatException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Invalid duration format", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(getContext(), "Failed to load allotted days", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Loads the planned vacation days from travel logs
     */
    private void loadPlannedVacationDays(){
        travelLogsRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                for(DataSnapshot trip : snapshot.getChildren()){
                    String startDateStr = trip.child("startTime").getValue(String.class);
                    String endDateStr = trip.child("endTime").getValue(String.class);

                    if(startDateStr == null || endDateStr == null){
                        continue;
                    }

                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);

                        if(startDate != null && endDate != null){
                            long difference = endDate.getTime() - startDate.getTime();
                            int tripDays = (int) (difference / (1000 * 60 * 60 * 24)) + 1;

                            totalPlannedDays += tripDays;
                        }
                    } catch(ParseException e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing trip dates", Toast.LENGTH_SHORT).show();
                    }
                }
                tvTotalDays.setText("Planned Vacation Days: " + totalPlannedDays);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(getContext(), "Failed to load travel logs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Visualizes the trip days using PieChart
     */
    private void visualizeTripDays(){
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

        data.setValueFormatter(new ValueFormatter(){
            @Override
            public String getFormattedValue(float value){
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
     * Loads the list of contributors from Firebase
     */
    private void loadContributors(){
        contributorsRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                contributorList.clear();
                for(DataSnapshot contributorSnapshot : snapshot.getChildren()){
                    Contributor contributor = contributorSnapshot.getValue(Contributor.class);
                    if(contributor != null){
                        contributorList.add(contributor);
                    }
                }
                contributorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(getContext(), "Failed to load contributors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Loads the sharedWith list and then fetches shared data from those users
     */
    private void loadSharedWithAndSharedData(){
        sharedWithRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                sharedWithList.clear();
                sharedNoteList.clear(); // Clear previous shared notes
                for(DataSnapshot sharedWithSnapshot : snapshot.getChildren()){
                    String ownerUserId = sharedWithSnapshot.getKey();
                    if(ownerUserId != null){
                        sharedWithList.add(ownerUserId);
                        fetchSharedData(ownerUserId);
                    }
                }
                sharedNotesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(getContext(), "Failed to load shared data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fetches shared data (locations, notes, etc.) from the owners
     */
    private void fetchSharedData(String ownerUserId){
        // Fetch shared user's notes
        DatabaseReference ownerNotesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(ownerUserId)
                .child("notes");

        ownerNotesRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot noteSnapshot : snapshot.getChildren()){
                    Note sharedNote = noteSnapshot.getValue(Note.class);
                    if(sharedNote != null){
                        sharedNoteList.add(sharedNote);
                    }
                }
                // Notify adapter after all shared notes are added
                sharedNotesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Log.e("LogisticsFragment", "Failed to fetch shared notes from " + ownerUserId, error.toException());
                Toast.makeText(getContext(), "Failed to fetch shared notes from a contributor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows a dialog to invite a new user by email
     */
    private void showInviteUserDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Invite User");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Enter user's email");
        builder.setView(input);

        builder.setPositiveButton("Invite", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if(!email.isEmpty()){
                inviteUserByEmail(email);
            } else {
                Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Invites a user by their email
     */
    private void inviteUserByEmail(String email){
        // Normalize email to lowercase to match stored emails
        String normalizedEmail = email.toLowerCase().trim();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("email").equalTo(normalizedEmail);

        Log.d("InviteUser", "Attempting to invite email: " + normalizedEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    Log.d("InviteUser", "User(s) found: " + snapshot.getChildrenCount());
                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        String invitedUserId = userSnapshot.getKey();
                        String invitedUserEmail = userSnapshot.child("email").getValue(String.class);
                        Log.d("InviteUser", "Found user: " + invitedUserId + " Email: " + invitedUserEmail);

                        if(invitedUserId != null && invitedUserEmail != null){
                            // Check if already a contributor
                            contributorsRef.child(invitedUserId).addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot contributorSnapshot){
                                    if(contributorSnapshot.exists()){
                                        Log.d("InviteUser", "User is already a contributor: " + invitedUserEmail);
                                        Toast.makeText(getContext(), "User is already a contributor", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Add to contributors
                                        Contributor newContributor = new Contributor(invitedUserId, invitedUserEmail);
                                        contributorsRef.child(invitedUserId).setValue(newContributor)
                                                .addOnCompleteListener(task -> {
                                                    if(task.isSuccessful()){
                                                        Log.d("InviteUser", "User invited successfully: " + invitedUserEmail);
                                                        Toast.makeText(getContext(), "User invited successfully", Toast.LENGTH_SHORT).show();

                                                        // Also, update User B's sharedWith list to include User A
                                                        DatabaseReference sharedWithRef = FirebaseDatabase.getInstance()
                                                                .getReference("users")
                                                                .child(invitedUserId)
                                                                .child("sharedWith")
                                                                .child(userId); // userId is User A's UID

                                                        sharedWithRef.setValue(true)
                                                                .addOnCompleteListener(sharedTask -> {
                                                                    if(sharedTask.isSuccessful()){
                                                                        Log.d("InviteUser", "User A added to User B's sharedWith list");
                                                                    } else {
                                                                        Log.e("InviteUser", "Failed to add User A to User B's sharedWith list", sharedTask.getException());
                                                                    }
                                                                });

                                                    } else {
                                                        Log.e("InviteUser", "Failed to invite user: " + invitedUserEmail, task.getException());
                                                        Toast.makeText(getContext(), "Failed to invite user", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error){
                                    Log.e("InviteUser", "Error checking contributor", error.toException());
                                    Toast.makeText(getContext(), "Error checking contributor", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    Log.d("InviteUser", "No user found with email: " + normalizedEmail);
                    Toast.makeText(getContext(), "User with this email does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Log.e("InviteUser", "Error searching for user", error.toException());
                Toast.makeText(getContext(), "Error searching for user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
