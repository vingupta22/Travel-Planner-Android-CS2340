package model;

/**
 * Represents an accommodation reservation with details such as location, room type,
 * check-in and check-out dates, and number of rooms.
 */
public class Accommodation {
    private String location;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;
    private int numberOfRooms;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(Accommodation.class).
     */
    public Accommodation() {
    }

    /**
     * Constructs an Accommodation with specified details.
     *
     * @param location the location of the accommodation
     * @param roomType the type of room reserved
     * @param checkInDate the check-in date for the reservation
     * @param checkOutDate the check-out date for the reservation
     * @param numberOfRooms the number of rooms reserved
     */
    public Accommodation(String location, String roomType, String checkInDate, String checkOutDate, int numberOfRooms) {
        this.location = location;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
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
     * Returns the type of room reserved.
     *
     * @return the room type as a String
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * Returns the check-in date of the reservation.
     *
     * @return the check-in date as a String
     */
    public String getCheckInDate() {
        return checkInDate;
    }

    /**
     * Returns the check-out date of the reservation.
     *
     * @return the check-out date as a String
     */
    public String getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Returns the number of rooms reserved.
     *
     * @return the number of rooms as an int
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * Sets the location of the accommodation.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the type of room reserved.
     *
     * @param roomType the room type to set
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    /**
     * Sets the check-in date of the reservation.
     *
     * @param checkInDate the check-in date to set
     */
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * Sets the check-out date of the reservation.
     *
     * @param checkOutDate the check-out date to set
     */
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * Sets the number of rooms reserved.
     *
     * @param numberOfRooms the number of rooms to set
     */
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
}
