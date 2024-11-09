package model;

public class Accommodation {
    private String location;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;
    private int numberOfRooms;

    // Default constructor required for calls to DataSnapshot.getValue(Accommodation.class)
    public Accommodation() {
    }

    public Accommodation(String location, String roomType, String checkInDate, String checkOutDate, int numberOfRooms) {
        this.location = location;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
    }

    // Getters
    public String getLocation() { return location; }
    public String getRoomType() { return roomType; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public int getNumberOfRooms() { return numberOfRooms; }

    // Setters
    public void setLocation(String location) { this.location = location; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setNumberOfRooms(int numberOfRooms) { this.numberOfRooms = numberOfRooms; }
}