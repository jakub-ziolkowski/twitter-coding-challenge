package com.hsbc.twitter.domain.user.exceptions;

public class FollowingException extends RuntimeException {

    public FollowingException() {
        super("You cant follow yourself");
    }

}
