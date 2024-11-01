package com.example.CS2340FAC_Team41.view;

public class DiningReservation {
    public String location;
    public String website;
    public String time;
    public String reviews;

    public DiningReservation() {
        // Default constructor required for calls to DataSnapshot.getValue(DiningReservation.class)
    }

    public DiningReservation(String location, String website, String time, String reviews) {
        this.location = location;
        this.website = website;
        this.time = time;
        this.reviews = reviews;
    }
}

