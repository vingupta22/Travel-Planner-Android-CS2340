package model;

import java.util.List;

public class TravelPost {
    private String postId;
    private String tripDuration;
    private String destinations;
    private String notes;

    public TravelPost() {
        // Default constructor required for calls to DataSnapshot.getValue(TravelPost.class)
    }

    public TravelPost(String postId, String tripDuration, String destinations, String notes) {
        this.postId = postId;
        this.tripDuration = tripDuration;
        this.destinations = destinations;
        this.notes = notes;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
