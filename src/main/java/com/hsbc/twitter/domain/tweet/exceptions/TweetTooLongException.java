package com.hsbc.twitter.domain.tweet.exceptions;

public class TweetTooLongException extends RuntimeException {

    public TweetTooLongException(String message) {
        super(message);
    }

}
