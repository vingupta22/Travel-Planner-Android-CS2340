package com.example.CS2340FAC_Team41.view;

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

public class DestinationsFragment extends Fragment {

    private LinearLayout travelForm;
    private Button logTravelButton, cancelButton, submitButton;
    private EditText inputLocation, inputStartTime, inputEndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_destinations, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        travelForm = view.findViewById(R.id.travel_form);
        logTravelButton = view.findViewById(R.id.btn_log_travel);
        cancelButton = view.findViewById(R.id.btn_cancel);
        submitButton = view.findViewById(R.id.btn_submit);
        inputLocation = view.findViewById(R.id.input_location);
        inputStartTime = view.findViewById(R.id.input_start_time);
        inputEndTime = view.findViewById(R.id.input_end_time);

        // Set onClickListener for the Log Travel button
        logTravelButton.setOnClickListener(v -> {
            travelForm.setVisibility(View.VISIBLE); // Show the form
        });

        // Set onClickListener for the Cancel button
        cancelButton.setOnClickListener(v -> {
            travelForm.setVisibility(View.GONE); // Hide the form
            clearInputs(); // Clear input fields
        });

        // Set onClickListener for the Submit button
        submitButton.setOnClickListener(v -> {
            String location = inputLocation.getText().toString();
            String startTime = inputStartTime.getText().toString();
            String endTime = inputEndTime.getText().toString();

            if (location.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle form submission (you can add logic to save the data)
                Toast.makeText(getActivity(), "Travel Logged!", Toast.LENGTH_SHORT).show();
                travelForm.setVisibility(View.GONE); // Hide the form
                clearInputs(); // Clear input fields
            }
        });
    }

    // Helper method to clear input fields
    private void clearInputs() {
        inputLocation.setText("");
        inputStartTime.setText("");
        inputEndTime.setText("");
    }
}
