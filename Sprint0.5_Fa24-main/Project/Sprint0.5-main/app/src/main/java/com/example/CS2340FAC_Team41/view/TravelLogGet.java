package com.example.CS2340FAC_Team41.view;

public class TravelLogGet {
    private String endTime;
    private String location;
    private String startTime;

    /**
     * Default constructor
     */
    public TravelLogGet() {

    }

    /**
     * Gets the travel logs
     * @param endTime time it ends
     * @param location the location of destination
     * @param startTime time it starts
     */
    public TravelLogGet(String endTime, String location, String startTime) {
        this.endTime = endTime;
        this.location = location;
        this.startTime = startTime;
    }

    /**
     * Returns the end time
     * @return end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time
     * @param endTime the time you want to end
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Receives the location
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location
     * @param location the location you want to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the start time
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time
     * @param startTime the time it starts
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}