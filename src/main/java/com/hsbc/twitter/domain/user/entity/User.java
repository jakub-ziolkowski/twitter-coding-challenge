package com.hsbc.twitter.domain.user.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

    private final String login;

    private final Set<User> followed;

    public User(String login) {
        this.login = login;
        this.followed = new HashSet<>();
    }

    public boolean follow(User userToFollow){
        return followed.add(userToFollow);
    }

    public String getLogin() {
        return login;
    }

    public Set<User> getFollowed() {
        return followed;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (this == object) return true;
        return Objects.equals(login, ((User)object).login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", followed=" + followed +
                '}';
    }
}

