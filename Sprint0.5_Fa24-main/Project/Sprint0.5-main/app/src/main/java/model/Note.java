package model;

import java.util.Objects;

/**
 * Represents a note with a user ID, content, and timestamp.
 */
public class Note {
    private String userId;
    private String content;
    private long timestamp;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(Note.class).
     */
    public Note() {
    }

    /**
     * Constructs a Note with the specified user ID, content, and timestamp.
     *
     * @param userId    the ID of the user who created the note
     * @param content   the content of the note
     * @param timestamp the time the note was created, in milliseconds
     */
    public Note(String userId, String content, long timestamp) {
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * Returns the user ID of the note's creator.
     *
     * @return the user ID as a String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the content of the note.
     *
     * @return the note content as a String
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the timestamp of when the note was created.
     *
     * @return the timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Compares this note to the specified object for equality. Two notes are
     * considered equal if they have the same user ID, content, and timestamp.
     *
     * @param obj the object to compare with
     * @return true if this note is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Note)) {
            return false;
        }
        Note other = (Note) obj;
        return userId.equals(other.userId) && content.equals(other.content) && timestamp == other.timestamp;
    }

    /**
     * Returns a hash code value for the note based on user ID, content, and timestamp.
     *
     * @return the hash code as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, content, timestamp);
    }
}
