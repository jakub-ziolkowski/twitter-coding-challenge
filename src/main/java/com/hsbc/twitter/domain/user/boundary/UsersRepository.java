package com.hsbc.twitter.domain.user.boundary;

import com.hsbc.twitter.domain.user.entity.User;

import java.util.Optional;

public interface UsersRepository {

    Optional<User> getUser(String login);

    User createUser(String login);

    void reset();
}
