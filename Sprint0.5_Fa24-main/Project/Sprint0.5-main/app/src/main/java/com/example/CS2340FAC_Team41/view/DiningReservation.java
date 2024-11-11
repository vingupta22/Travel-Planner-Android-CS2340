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

    public DiningReservation(String location, Date dateTime, String website, String reviews) {
        this.location = location;
        this.dateTime = dateTime;
        this.website = website;
        this.reviews = reviews;
    }

    public String getLocation() {
        return location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getWebsite() {
        return website;
    }

    public String getReviews() {
        return reviews;
    }

    public String getDate() {
        return dateFormatter.format(dateTime);
    }

    public String getTime() {
        return timeFormatter.format(dateTime);
    }
}