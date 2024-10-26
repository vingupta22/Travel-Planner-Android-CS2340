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
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.CS2340FAC_Team41.R;
import com.example.CS2340FAC_Team41.databinding.ActivityMainBinding;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DestinationsFragment extends Fragment {

    private LinearLayout travelForm, vacationForm;
    private Button logTravelButton, calculateVacationButton, cancelButton, submitButton, calculateButton;
    private EditText inputLocation, inputStartTime, inputEndTime, inputStartDate, inputEndDate, inputDuration;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_destinations, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
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

        // Log Travel button opens the Travel Form
        logTravelButton.setOnClickListener(v -> {
            travelForm.setVisibility(View.VISIBLE);
            vacationForm.setVisibility(View.GONE);
        });

        // Calculate Vacation Time button opens the Vacation Form
        calculateVacationButton.setOnClickListener(v -> {
            vacationForm.setVisibility(View.VISIBLE);
            travelForm.setVisibility(View.GONE);
        });

        // Cancel button hides both forms
        cancelButton.setOnClickListener(v -> {
            travelForm.setVisibility(View.GONE);
            vacationForm.setVisibility(View.GONE);
            clearInputs();
        });

        // Submit button logic
        submitButton.setOnClickListener(v -> {
            String location = inputLocation.getText().toString();
            String startTime = inputStartTime.getText().toString();
            String endTime = inputEndTime.getText().toString();

            if (location.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Travel Logged!", Toast.LENGTH_SHORT).show();
                travelForm.setVisibility(View.GONE);
                clearInputs();
            }
        });

        // Calculate button logic for vacation time
        calculateButton.setOnClickListener(v -> calculateVacationTime());
    }

    private void clearInputs() {
        inputLocation.setText("");
        inputStartTime.setText("");
        inputEndTime.setText("");
        inputStartDate.setText("");
        inputEndDate.setText("");
        inputDuration.setText("");
    }

    private void calculateVacationTime() {
        String startDateStr = inputStartDate.getText().toString();
        String endDateStr = inputEndDate.getText().toString();
        String durationStr = inputDuration.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                // Calculate the duration between the two dates
                Date startDate = sdf.parse(startDateStr);
                Date endDate = sdf.parse(endDateStr);

                long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
                long days = diffInMillis / (1000 * 60 * 60 * 24);

                inputDuration.setText(String.valueOf(days));
                Toast.makeText(getActivity(), "Duration calculated!", Toast.LENGTH_SHORT).show();
            } else if (!startDateStr.isEmpty() && !durationStr.isEmpty()) {
                // Calculate the end date based on start date and duration
                Date startDate = sdf.parse(startDateStr);
                long durationInMillis = Long.parseLong(durationStr) * 24 * 60 * 60 * 1000;

                Date endDate = new Date(startDate.getTime() + durationInMillis);
                inputEndDate.setText(sdf.format(endDate));
                Toast.makeText(getActivity(), "End date calculated!", Toast.LENGTH_SHORT).show();
            } else if (!endDateStr.isEmpty() && !durationStr.isEmpty()) {
                // Calculate the start date based on end date and duration
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