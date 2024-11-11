package model;

import java.util.Objects;

public class Note {
    private String userId;
    private String content;
    private long timestamp;

    // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    public Note() {}

    public Note(String userId, String content, long timestamp) {
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Override equals and hashCode for proper comparison in lists
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Note)) return false;
        Note other = (Note) obj;
        return userId.equals(other.userId) && content.equals(other.content) && timestamp == other.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, content, timestamp);
    }
}
