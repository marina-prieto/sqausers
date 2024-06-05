package dissw24.sqausers.model;

import java.time.Instant;
import java.time.Duration;

public class Token {

    private static final Duration DEFAULT_DURATION = Duration.ofMinutes(15);
    private String id;
    private User user;
    private Instant expiryTime;

    public Token(String id, User user) {
        this.id = id;
        this.user = user;
        this.expiryTime = Instant.now().plus(DEFAULT_DURATION);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryTime);
    }

    public void extendExpiryTime() {
        this.expiryTime = Instant.now().plus(DEFAULT_DURATION);
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }
}