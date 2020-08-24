package com.hsbc.twitter.domain.tweet.boundary;

import com.hsbc.twitter.domain.tweet.entity.Tweet;
import com.hsbc.twitter.domain.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TweetsRepository {

    Tweet createTweet(User user, String message);

    Optional<Tweet> getTweet(UUID id);

    List<Tweet> getUserWall(User user);

    List<Tweet> getUserTimeline(User user);

    void reset();
}
