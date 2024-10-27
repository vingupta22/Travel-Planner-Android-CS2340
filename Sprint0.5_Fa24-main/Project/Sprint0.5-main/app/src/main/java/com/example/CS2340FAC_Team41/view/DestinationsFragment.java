package com.example.CS2340FAC_Team41.view;

import java.text.SimpleDateFormat;
import java.util.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.CS2340FAC_Team41.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;

public class DestinationsFragment extends Fragment {

    private LinearLayout travelForm, vacationForm;
    private Button logTravelButton, calculateVacationButton, cancelButton, submitButton, calculateButton;
    private EditText inputLocation, inputStartTime, inputEndTime, inputStartDate, inputEndDate, inputDuration;
    private DatabaseReference mDatabase;
    private String userId;
    private RecyclerView recyclerView;
    private DestinationsAdapter destinationsAdapter;
    private List<Map<String, String>> travelLogs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_destinations, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        destinationsAdapter = new DestinationsAdapter(travelLogs);
        recyclerView.setAdapter(destinationsAdapter);

        fetchTravelLogs();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's ID
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId);

        travelForm = view.findViewById(R.id.travel_form);
        vacationForm = view.findViewById(R.id.vacation_form);
        logTravelButton = view.findViewById(R.id.btn_log_travel);
        calculateVacationButton = view.findViewById(R.id.btn_calculate_vacation_time);
        cancelButton = view.findViewById(R.id.btn_cancel);
        submitButton = view.findViewById(R.id.btn_submit);
        calculateButton = view.findViewById(R.id.btn_calculate);

        inputLocation = view.findViewById(R.id.input_location);
        inputStartTime = view.findViewById(R.id.input_start_time);
        inputEndTime = view.findViewById(R.id.input_end_time);
        inputStartDate = view.findViewById(R.id.input_start_date);
        inputEndDate = view.findViewById(R.id.input_end_date);
        inputDuration = view.findViewById(R.id.input_duration);

        logTravelButton.setOnClickListener(v -> {
            travelForm.setVisibility(View.VISIBLE);
            vacationForm.setVisibility(View.GONE);
        });

        calculateVacationButton.setOnClickListener(v -> {
            vacationForm.setVisibility(View.VISIBLE);
            travelForm.setVisibility(View.GONE);
        });

        cancelButton.setOnClickListener(v -> {
            travelForm.setVisibility(View.GONE);
            vacationForm.setVisibility(View.GONE);
            clearInputs();
        });

        submitButton.setOnClickListener(v -> {
            String location = inputLocation.getText().toString();
            String startTime = inputStartTime.getText().toString();
            String endTime = inputEndTime.getText().toString();

            if (location.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Store travel data in Firebase
                saveTravelData(location, startTime, endTime);
                Toast.makeText(getActivity(), "Travel Logged!", Toast.LENGTH_SHORT).show();
                travelForm.setVisibility(View.GONE);
                clearInputs();
            }
        });

        calculateButton.setOnClickListener(v -> {
            calculateVacationTime();
            saveVacationData();
        });
    }

    private void clearInputs() {
        inputLocation.setText("");
        inputStartTime.setText("");
        inputEndTime.setText("");
        inputStartDate.setText("");
        inputEndDate.setText("");
        inputDuration.setText("");
    }

    private void fetchTravelLogs() {
        mDatabase.child("travelLogs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                travelLogs.clear();
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    Map<String, String> log = (Map<String, String>) logSnapshot.getValue();
                    calculateDays(log);  // Calculate days for each log
                    travelLogs.add(log);
                }
                destinationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to fetch travel logs.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateDays(Map<String, String> log) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(log.get("startTime"));
            Date endDate = sdf.parse(log.get("endTime"));
            long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
            long days = diffInMillis / (1000 * 60 * 60 * 24);
            log.put("days", String.valueOf(days));
        } catch (Exception e) {
            log.put("days", "N/A");
        }
    }

    private void calculateVacationTime() {
        String startDateStr = inputStartDate.getText().toString();
        String endDateStr = inputEndDate.getText().toString();
        String durationStr = inputDuration.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                Date startDate = sdf.parse(startDateStr);
                Date endDate = sdf.parse(endDateStr);

                long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
                long days = diffInMillis / (1000 * 60 * 60 * 24);

                inputDuration.setText(String.valueOf(days));
                Toast.makeText(getActivity(), "Duration calculated!", Toast.LENGTH_SHORT).show();
            } else if (!startDateStr.isEmpty() && !durationStr.isEmpty()) {
                Date startDate = sdf.parse(startDateStr);
                long durationInMillis = Long.parseLong(durationStr) * 24 * 60 * 60 * 1000;

                Date endDate = new Date(startDate.getTime() + durationInMillis);
                inputEndDate.setText(sdf.format(endDate));
                Toast.makeText(getActivity(), "End date calculated!", Toast.LENGTH_SHORT).show();
            } else if (!endDateStr.isEmpty() && !durationStr.isEmpty()) {
                Date endDate = sdf.parse(endDateStr);
                long durationInMillis = Long.parseLong(durationStr) * 24 * 60 * 60 * 1000;

                Date startDate = new Date(endDate.getTime() - durationInMillis);
                inputStartDate.setText(sdf.format(startDate));
                Toast.makeText(getActivity(), "Start date calculated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please fill in at least two fields!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid input! Please use yyyy-MM-dd format for dates.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTravelData(String location, String startTime, String endTime) {
        String key = mDatabase.child("travelLogs").push().getKey();
        Map<String, String> travelData = new HashMap<>();
        travelData.put("location", location);
        travelData.put("startTime", startTime);
        travelData.put("endTime", endTime);

        mDatabase.child("travelLogs").child(key).setValue(travelData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Travel data saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to save data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveVacationData() {
        String startDate = inputStartDate.getText().toString();
        String endDate = inputEndDate.getText().toString();
        String duration = inputDuration.getText().toString();

        if (!startDate.isEmpty() || !endDate.isEmpty() || !duration.isEmpty()) {
            String key = mDatabase.child("vacationLogs").push().getKey();
            Map<String, String> vacationData = new HashMap<>();
            vacationData.put("startDate", startDate);
            vacationData.put("endDate", endDate);
            vacationData.put("duration", duration);

            mDatabase.child("vacationLogs").child(key).setValue(vacationData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Vacation data saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to save vacation data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}