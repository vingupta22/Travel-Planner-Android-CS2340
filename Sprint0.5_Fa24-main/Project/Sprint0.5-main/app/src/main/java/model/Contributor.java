package model;

/**
 * Represents a contributor with a unique user ID and email.
 */
public class Contributor {
    private String userId;
    private String email;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(Contributor.class).
     */
    public Contributor() {
    }

    /**
     * Constructs a Contributor with a specified user ID and email.
     *
     * @param userId the unique ID of the contributor
     * @param email the email address of the contributor
     */
    public Contributor(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    /**
     * Returns the user ID of the contributor.
     *
     * @return the user ID as a String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the contributor.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the email address of the contributor.
     *
     * @return the email as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the contributor.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
