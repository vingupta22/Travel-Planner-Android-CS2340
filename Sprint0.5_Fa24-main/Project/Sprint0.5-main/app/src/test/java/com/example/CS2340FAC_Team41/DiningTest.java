package com.example.CS2340FAC_Team41;

import static org.junit.Assert.*;

import com.example.CS2340FAC_Team41.view.DiningReservation;
import com.example.CS2340FAC_Team41.view.DiningReservationAdapter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DiningTest {

    private DiningReservation reservation;

    @Before
    public void setUp() {
        // Set up a valid dining reservation for testing
        reservation = new DiningReservation("New York", createFutureDate(), "18:00", "https://example.com");
    }

    // Author Anish Cheraku
    @Test
    public void testLocationIsNotEmpty() {
        // Assert that the location is not empty
        assertFalse("Location should not be empty", reservation.getLocation().isEmpty());
    }

    @Test
    public void testDateIsNotNull() {
        // Assert that the date is not null
        assertNotNull("Date should not be null", reservation.getDateTime());
    }


    // Author Dev Patel
    @Test
    public void testTimeIsNotEmpty() {
        // Assert that the time is not empty
        assertFalse("Time should not be empty", reservation.getTime().isEmpty());
    }

    @Test
    public void testWebsiteIsNotEmpty() {
        // Assert that the website is not empty
        assertFalse("Website should not be empty", reservation.getWebsite().isEmpty());
    }

    // Author Viraj Kulkarni
    @Test
    public void testReviewsAreNotEmpty() {
        // Assert that the reviews field is not empty
        assertFalse("Reviews should not be empty", reservation.getReviews().isEmpty());
    }

    @Test
    public void testUpcomingReservation() {
        // Check if the reservation date is in the future
        assertTrue("Reservation should be upcoming", reservation.getDateTime().after(new Date()));
    }

    @Test
    public void testExpiredReservation() {
        // Arrange a reservation with a past date
        reservation = new DiningReservation("New York", createPastDate(), "18:00", "https://example.com");

        // Assert that this reservation is expired
        assertTrue("Reservation should be expired", reservation.getDateTime().before(new Date()));
    }

    private Date createFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // 1 day in the future
        return calendar.getTime();
    }

    private Date createPastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 1 day in the past
        return calendar.getTime();
    }
}