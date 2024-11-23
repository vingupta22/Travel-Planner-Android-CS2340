package model;

public class TravelPost {
    private String postId;
    private String tripDuration;
    private String destinations;
    private String notes;
    private String startDate;
    private String endDate;
    private String accommodations;
    private String diningReservations;
    private int rating;

    // Default constructor required for calls to DataSnapshot.getValue(TravelPost.class)
    public TravelPost() {
    }

    // Constructor with all fields
    public TravelPost(String postId, String tripDuration, String destinations, String notes,
                      String startDate, String endDate, String accommodations,
                      String diningReservations, int rating) {
        this.postId = postId;
        this.tripDuration = tripDuration;
        this.destinations = destinations;
        this.notes = notes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
        this.rating = rating;
    }

    // Getters and Setters
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(String accommodations) {
        this.accommodations = accommodations;
    }

    public String getDiningReservations() {
        return diningReservations;
    }

    public void setDiningReservations(String diningReservations) {
        this.diningReservations = diningReservations;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
