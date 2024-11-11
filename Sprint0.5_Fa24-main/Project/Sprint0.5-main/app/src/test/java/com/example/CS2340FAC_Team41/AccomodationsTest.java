package com.example.CS2340FAC_Team41;
import static org.junit.Assert.*;

import com.example.CS2340FAC_Team41.view.AccommodationAdapter;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Accommodation;


public class AccomodationsTest {
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }


    // Author Vinay Gupta
    @Test
    public void testLocationIsNotEmpty() {
        Accommodation accommodation = new Accommodation("New York", "Single", "2024-12-01", "2024-12-10", 1);

        assertFalse("Location should not be empty", accommodation.getLocation().isEmpty());
    }

    @Test
    public void testRoomTypeIsNotEmpty() {
        Accommodation accommodation = new Accommodation("New York", "Single", "2024-12-01", "2024-12-10", 1);

        assertFalse("Room type should not be empty", accommodation.getRoomType().isEmpty());
    }

    @Test
    public void testCheckInDateIsNotEmpty() {
        Accommodation accommodation = new Accommodation("New York", "Single", "2024-12-01", "2024-12-10", 1);

        assertFalse("Check-in date should not be empty", accommodation.getCheckInDate().isEmpty());
    }


    // Author Justin Zheng
    @Test
    public void testCheckOutDateIsNotEmpty() {
        Accommodation accommodation = new Accommodation("New York", "Single", "2024-12-01", "2024-12-10", 1);
        assertFalse("Check-out date should not be empty", accommodation.getCheckOutDate().isEmpty());
    }

    @Test
    public void testNumberOfRoomsIsGreaterThanZero() {
        Accommodation accommodation = new Accommodation("New York", "Single", "2024-12-01", "2024-12-10", 1);

        assertTrue("Number of rooms should be greater than zero", accommodation.getNumberOfRooms() > 0);
    }

    @Test
    public void testCheckInDateBeforeCheckOutDate() {
        Accommodation accommodation = new Accommodation("New York", "Single", "2024-12-01", "2024-12-10", 1);

        boolean validDates = isCheckInBeforeCheckOut(accommodation.getCheckInDate(), accommodation.getCheckOutDate());

        assertTrue("Check-in date should be before check-out date", validDates);
    }

    private boolean isCheckInBeforeCheckOut(String checkInDate, String checkOutDate) {
        try {
            Date checkIn = dateFormat.parse(checkInDate);
            Date checkOut = dateFormat.parse(checkOutDate);
            return checkIn != null && checkOut != null && checkIn.before(checkOut);
        } catch (ParseException e) {
            return false;
        }
    }
}
