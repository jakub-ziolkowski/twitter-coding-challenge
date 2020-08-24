package com.hsbc.twitter.domain.tweet.entity;

import com.hsbc.twitter.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Tweet {

    private final UUID id;

    private final User user;

    private final String message;

    private final LocalDateTime created;

    public Tweet(UUID id, User user, String message, LocalDateTime created) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.created = created;
    }

    public Tweet(User user, String message) {
        this(UUID.randomUUID(), user, message, LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (this == object) return true;
        return Objects.equals(id, ((Tweet)object).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", user=" + user +
                ", message='" + message + '\'' +
                ", created=" + created +
                '}';
    }
}
