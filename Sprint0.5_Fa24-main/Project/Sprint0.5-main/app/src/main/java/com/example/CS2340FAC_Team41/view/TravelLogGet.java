package com.example.CS2340FAC_Team41.view;

public class TravelLogGet {
    private String endTime;
    private String location;
    private String startTime;

    public TravelLogGet() {

    }

    public TravelLogGet(String endTime, String location, String startTime) {
        this.endTime = endTime;
        this.location = location;
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
