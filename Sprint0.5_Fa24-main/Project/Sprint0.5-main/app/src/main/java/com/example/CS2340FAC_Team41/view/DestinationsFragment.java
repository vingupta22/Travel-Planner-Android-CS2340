package com.example.CS2340FAC_Team41.view;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.CS2340FAC_Team41.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DestinationsFragment extends Fragment {

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private LinearLayout travelForm, vacationForm;
    private Button logTravelButton, calculateVacationButton, cancelButton, submitButton, calculateButton;
    private EditText inputLocation, inputStartTime, inputEndTime, inputStartDate, inputEndDate, inputDuration;
    private DatabaseReference mDatabase;
    private String userId;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_destinations, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            String location = inputLocation.getText().toString().trim();
            String startTime = inputStartTime.getText().toString().trim();
            String endTime = inputEndTime.getText().toString().trim();

            if(location.length()==0){
                location="Location";
            }
            if(startTime.length()==0){
                startTime="2024-02-02";
            }
            if(endTime.length()==0){
                endTime="2024-02-02";
            }

            if (location.isEmpty() || !isValidDate(startTime) || !isValidDate(endTime)) {
                Toast.makeText(getActivity(), "Please enter valid data in YYYY-MM-DD format.", Toast.LENGTH_SHORT).show();
            } else {
                saveTravelData(location, startTime, endTime);
                Toast.makeText(getActivity(), "Travel Logged!", Toast.LENGTH_SHORT).show();
                travelForm.setVisibility(View.GONE);
                clearInputs();
            }
        });

        calculateButton.setOnClickListener(v -> {
            if (isValidDate(inputStartDate.getText().toString().trim()) &&
                    isValidDate(inputEndDate.getText().toString().trim())) {
                calculateVacationTime();
                saveVacationData();
            } else {
                Toast.makeText(getActivity(), "Please enter dates in YYYY-MM-DD format.", Toast.LENGTH_SHORT).show();
            }
        });

        listView = view.findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, list);
        listView.setAdapter(adapter);

        DatabaseReference ref = mDatabase.child("travelLogs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                long children = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TravelLogGet info = snapshot.getValue(TravelLogGet.class);
                    assert info != null;
                    String loc = info.getLocation();
                    long days = calculateDuration(info.getStartTime(), info.getEndTime());
                    if (children <= 5) {
                        list.add(loc + "\t\t\t" + days + " days planned");
                    }
                    children--;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load travel logs.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    private long calculateDuration(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
            return diffInMillis / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            return 0;
        }
    }

    private void clearInputs() {
        inputLocation.setText("");
        inputStartTime.setText("");
        inputEndTime.setText("");
        inputStartDate.setText("");
        inputEndDate.setText("");
        inputDuration.setText("");
    }

    private void saveTravelData(String location, String startTime, String endTime) {
        String key = mDatabase.child("travelLogs").push().getKey();
        Map<String, String> travelData = new HashMap<>();
        travelData.put("location", location);
        travelData.put("startTime", startTime);
        travelData.put("endTime", endTime);

        mDatabase.child("travelLogs").child(key).setValue(travelData)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Failed to save data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveVacationData() {
        String startDate = inputStartDate.getText().toString().trim();
        String endDate = inputEndDate.getText().toString().trim();
        String duration = inputDuration.getText().toString().trim();

        if (!startDate.isEmpty() && !endDate.isEmpty() && !duration.isEmpty()) {
            String key = mDatabase.child("vacationLogs").push().getKey();
            Map<String, String> vacationData = new HashMap<>();
            vacationData.put("startDate", startDate);
            vacationData.put("endDate", endDate);
            vacationData.put("duration", duration);

            mDatabase.child("vacationLogs").child(key).setValue(vacationData)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Failed to save vacation data.", Toast.LENGTH_SHORT).show();
                        }
                    });
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
}