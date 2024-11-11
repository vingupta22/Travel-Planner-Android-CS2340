package com.example.CS2340FAC_Team41.view;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.CS2340FAC_Team41.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Accommodation;
import viewmodel.AccommodationViewModel;
import viewmodel.SortByCheckInDate;
import viewmodel.SortByCheckOutDate;

public class AccommodationsFragment extends Fragment {

    private AccommodationViewModel accommodationViewModel;
    private EditText inputLocation;
    private EditText inputRoomType;
    private EditText inputCheckInDate;
    private EditText inputCheckOutDate;
    private EditText inputNumberOfRooms;
    private Button submitButton;
    private Button sortCheckInButton;
    private Button sortCheckOutButton;
    private RecyclerView accommodationRecyclerView;
    private AccommodationAdapter adapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_accommodation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        // RecyclerView setup for accommodation list
        accommodationRecyclerView = view.findViewById(R.id.accommodation_recycler_view);
        accommodationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccommodationAdapter();
        accommodationRecyclerView.setAdapter(adapter);

        // Observe LiveData for changes
        accommodationViewModel.getAccommodationsLiveData().observe(getViewLifecycleOwner(), accommodations -> {
            adapter.setAccommodations(accommodations);
        });

        // Set up form elements and buttons
        setupForm(view);
    }


    /**
     * Initializes the form UI elements and sets up click listeners for user actions.
     *
     * @param view the root view containing the form elements
     *
     * <p>This method sets up various input fields and buttons used for capturing accommodation details:
     * location, room type, check-in and check-out dates, and the number of rooms. Additionally, it
     * assigns click listeners to the submit and sort buttons for handling user actions.</p>
     *
     *
     */
    private void setupForm(View view) {
        inputLocation = view.findViewById(R.id.input_location);
        inputRoomType = view.findViewById(R.id.input_room_type);
        inputCheckInDate = view.findViewById(R.id.input_check_in_date);
        inputCheckOutDate = view.findViewById(R.id.input_check_out_date);
        inputNumberOfRooms = view.findViewById(R.id.input_number_of_rooms);

        submitButton = view.findViewById(R.id.btn_submit_accommodation);
        sortCheckInButton = view.findViewById(R.id.btn_sort_check_in);
        sortCheckOutButton = view.findViewById(R.id.btn_sort_check_out);

        submitButton.setOnClickListener(v -> addAccommodation());
        sortCheckInButton.setOnClickListener(v -> accommodationViewModel.setSortStrategy(new SortByCheckInDate()));
        sortCheckOutButton.setOnClickListener(v -> accommodationViewModel.setSortStrategy(new SortByCheckOutDate()));
    }

    /**
     * Validates and adds a new accommodation entry based on user inputs.
     * Displays error messages if input fields are invalid or duplicate entries are detected.
     */
    private void addAccommodation() {
        String location = inputLocation.getText().toString();
        String roomType = inputRoomType.getText().toString();
        String checkInDate = inputCheckInDate.getText().toString();
        String checkOutDate = inputCheckOutDate.getText().toString();
        int numberOfRooms;

        // Check if number of rooms is valid
        try {
            numberOfRooms = Integer.parseInt(inputNumberOfRooms.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number of rooms", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for empty fields
        if (TextUtils.isEmpty(location) || TextUtils.isEmpty(roomType) || TextUtils.isEmpty(checkInDate)
                || TextUtils.isEmpty(checkOutDate)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check date format and logical consistency
        if (!isValidDateFormat(checkInDate) || !isValidDateFormat(checkOutDate)
                || !isCheckInBeforeCheckOut(checkInDate, checkOutDate)) {
            Toast.makeText(getContext(),
                    "Please enter valid dates (format: YYYY-MM-DD) and ensure check-in is before check-out",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for duplicate entries
        if (isDuplicateEntry(location, checkInDate, checkOutDate)) {
            Toast.makeText(getContext(), "This accommodation entry already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed with adding accommodation
        Accommodation accommodation = new Accommodation(location, roomType, checkInDate, checkOutDate, numberOfRooms);
        accommodationViewModel.addAccommodation(accommodation);
        Toast.makeText(getContext(), "Accommodation added", Toast.LENGTH_SHORT).show();

        // Clear form fields
        clearInputs();
    }

    /**
     * Checks if a date string is in the correct format (YYYY-MM-DD).
     *
     * @param date the date string to validate
     * @return true if the date format is valid, false otherwise
     */
    private boolean isValidDateFormat(String date) {
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Checks if the check-in date is before the check-out date.
     *
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if check-in is before check-out, false otherwise
     */
    private boolean isCheckInBeforeCheckOut(String checkInDate, String checkOutDate) {
        try {
            Date checkIn = dateFormat.parse(checkInDate);
            Date checkOut = dateFormat.parse(checkOutDate);
            return checkIn != null && checkOut != null && checkIn.before(checkOut);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Checks if an accommodation entry with the specified location and dates already exists.
     *
     * @param location the location of the accommodation
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if a duplicate entry exists, false otherwise
     */
    private boolean isDuplicateEntry(String location, String checkInDate, String checkOutDate) {
        List<Accommodation> accommodations = accommodationViewModel.getAccommodationsLiveData().getValue();
        if (accommodations != null) {
            for (Accommodation accommodation : accommodations) {
                if (accommodation.getLocation().equals(location)
                        && accommodation.getCheckInDate().equals(checkInDate)
                        && accommodation.getCheckOutDate().equals(checkOutDate)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Clears all input fields in the form.
     */
    private void clearInputs() {
        inputLocation.setText("");
        inputRoomType.setText("");
        inputCheckInDate.setText("");
        inputCheckOutDate.setText("");
        inputNumberOfRooms.setText("");
    }
}
