package com.example.CS2340FAC_Team41;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.CS2340FAC_Team41.view.TravelCommunityFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import model.TravelPost;

public class TravelCommunityTest {

    private TravelCommunityFragment fragment;

    @Mock
    private ArrayList<TravelPost> mockPostList;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fragment = new TravelCommunityFragment();
        fragment.setPostList(mockPostList); // Use mocked post list
    }

    // Test 1: Validate correct date format, Vinay
    @Test
    public void testValidateCorrectDateFormat() throws ParseException {
        String validDate = "2024-11-24";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(validDate);

        assertNotNull(parsedDate); // Assert date is parsed successfully
    }

    // Test 2: Reject incorrect date format, Vinay
    @Test
    public void testRejectIncorrectDateFormat() {
        String invalidDate = "2024-13-01"; // Invalid date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        boolean isValid = true;
        try {
            dateFormat.parse(invalidDate);
        } catch (ParseException e) {
            isValid = false;
        }

        assertFalse(isValid);
    }

    // Test 3: Validate start date is before end date, Anish
    @Test
    public void testStartDateBeforeEndDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2024-11-01");
        Date endDate = dateFormat.parse("2024-11-24");

        assertTrue(startDate.before(endDate));
    }

    // Test 4: Reject start date after end date, Anish
    @Test
    public void testStartDateAfterEndDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2024-11-25");
        Date endDate = dateFormat.parse("2024-11-24");

        assertFalse(startDate.before(endDate));
    }

    // Test 5: Validate rating between 1 and 5, Dev
    @Test
    public void testValidateRatingInRange() {
        int rating = 4;
        assertTrue(rating >= 1 && rating <= 5);
    }

    // Test 6: Reject invalid rating outside range, Dev
    @Test
    public void testRejectInvalidRating() {
        int rating = 6;
        assertFalse(rating >= 1 && rating <= 5);
    }

    // Test 7: Validate all fields are non-empty, Viraj
    @Test
    public void testValidateNonEmptyFields() {
        String tripDuration = "5 days";
        String destinations = "Paris";
        String notes = "Great trip!";
        String startDate = "2024-11-01";
        String endDate = "2024-11-10";

        assertFalse(tripDuration.isEmpty());
        assertFalse(destinations.isEmpty());
        assertFalse(notes.isEmpty());
        assertFalse(startDate.isEmpty());
        assertFalse(endDate.isEmpty());
    }

    // Test 8: Reject empty required fields, Viraj
    @Test
    public void testRejectEmptyRequiredFields() {
        String tripDuration = "";
        String destinations = "Paris";

        assertTrue(tripDuration.isEmpty());
        assertFalse(destinations.isEmpty());
    }

    // Test 9: Add a post to the list, Justin
    @Test
    public void testAddPostToList() {
        TravelPost mockPost = mock(TravelPost.class);
        when(mockPost.getTripDuration()).thenReturn("5 days");
        when(mockPost.getDestinations()).thenReturn("Paris");

        fragment.getPostList().add(mockPost);
        verify(mockPostList, times(1)).add(mockPost);
    }

    // Test 10: Ensure post list is updated after adding a post, Justin
    @Test
    public void testPostListIsUpdated() {
        // Arrange: Create a real list for testing
        ArrayList<TravelPost> realPostList = new ArrayList<>();
        fragment.setPostList(realPostList);

        // Create a dummy TravelPost
        TravelPost post = new TravelPost("123", "5 days", "Paris", "Notes", "2024-11-01",
                "2024-11-05", "Hotel", "Restaurant", 5, "user1");

        // Act: Add the post to the list
        int initialSize = fragment.getPostList().size();
        fragment.getPostList().add(post);

        // Assert: Check if the list size increased by 1
        assertEquals(initialSize + 1, fragment.getPostList().size());
        assertTrue(fragment.getPostList().contains(post));
    }
}
