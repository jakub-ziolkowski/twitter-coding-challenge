package com.hsbc.twitter.domain.tweet.controller;

import com.hsbc.twitter.domain.tweet.boundary.TweetsRepository;
import com.hsbc.twitter.domain.tweet.entity.Tweet;
import com.hsbc.twitter.domain.tweet.exceptions.TweetTooLongException;
import com.hsbc.twitter.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VolatileTweetsRepository implements TweetsRepository {

    @Value("${com.hsbc.twitter.config.max_post_length}")
    private int MAX_POST_LENGTH;

    private final List<Tweet> tweets = new LinkedList<>();

    @Override
    public Tweet createTweet(User user, String message) {
        if (message.length() > MAX_POST_LENGTH) {
            throw new TweetTooLongException("The message length has exceeded the maximum size limit of " + MAX_POST_LENGTH + " characters");
        }

        Tweet t = new Tweet(user, message);
        tweets.add(t);
        return t;
    }

    @Override
    public Optional<Tweet> getTweet(UUID id) {
        return tweets.stream()
                .filter(tweet -> tweet.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Tweet> getUserWall(User user) {
        return tweets.stream()
                .filter(tweet -> tweet.getUser().equals(user))
                .sorted(Comparator.comparing(Tweet::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Tweet> getUserTimeline(User user) {
        return user.getFollowed().stream()
                .flatMap(followed -> getUserWall(followed).stream())
                .sorted(Comparator.comparing(Tweet::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void reset() {
        tweets.clear();
    }

}
