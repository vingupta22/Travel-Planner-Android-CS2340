package com.example.CS2340FAC_Team41.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiningReservation {
    private String location;
    private Date dateTime;
    private String website;
    private String reviews;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

    /**
     * Constructs a new DiningReservation with the specified location, date and time, website, and reviews.
     *
     * @param location the location of the dining reservation
     * @param dateTime the date and time of the reservation
     * @param website the website associated with the reservation
     * @param reviews reviews of the dining location
     */
    public DiningReservation(String location, Date dateTime, String website, String reviews) {
        this.location = location;
        this.dateTime = dateTime;
        this.website = website;
        this.reviews = reviews;
    }

    /**
     * Returns the location of the accommodation.
     *
     * @return the location as a String
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the date and time of the accommodation.
     *
     * @return the date and time as a Date object
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Returns the website associated with the accommodation.
     *
     * @return the website as a String
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Returns reviews for the accommodation.
     *
     * @return the reviews as a String
     */
    public String getReviews() {
        return reviews;
    }

    /**
     * Returns the date portion of the accommodation's date and time.
     *
     * @return the formatted date as a String
     */
    public String getDate() {
        return dateFormatter.format(dateTime);
    }

    /**
     * Returns the time portion of the accommodation's date and time.
     *
     * @return the formatted time as a String
     */
    public String getTime() {
        return timeFormatter.format(dateTime);
    }

}