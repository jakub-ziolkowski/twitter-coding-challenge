package com.hsbc.twitter.domain.user.controller;

import com.hsbc.twitter.domain.user.boundary.UsersRepository;
import com.hsbc.twitter.domain.user.entity.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VolatileUsersRepository implements UsersRepository {

    private Set<User> users = new HashSet<>();

    @Override
    public Optional<User> getUser(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public User createUser(String login) {
        User user = new User(login);
        users.add(user);
        return user;
    }

    @Override
    public void reset() {
        users.clear();
    }

}
