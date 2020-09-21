package com.hsbc.twitter.domain;

import com.hsbc.twitter.api.dto.TweetDTO;
import com.hsbc.twitter.api.dto.UserDTO;
import com.hsbc.twitter.domain.tweet.boundary.TweetsRepository;
import com.hsbc.twitter.domain.tweet.exceptions.TweetTooLongException;
import com.hsbc.twitter.domain.user.boundary.UsersRepository;
import com.hsbc.twitter.domain.user.entity.User;
import com.hsbc.twitter.domain.user.exceptions.FollowingException;
import com.hsbc.twitter.domain.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterService {

    private UsersRepository usersRepository;

    private TweetsRepository tweetsRepository;

    @Autowired
    public TwitterService(UsersRepository usersRepository, TweetsRepository tweetsRepository) {
        this.usersRepository = usersRepository;
        this.tweetsRepository = tweetsRepository;
    }

    public TweetDTO createTweet(String login, String message) throws TweetTooLongException {
        return TweetDTO.toDTO(tweetsRepository.createTweet(getOrCreateNewUser(login), message));
    }

    public List<UserDTO> followUser(String login, String userToFollow) throws UserNotFoundException, FollowingException {
        User user = getUser(login);
        User toFollow = getUser(userToFollow);
        if (user.equals(toFollow)) throw new FollowingException();
        user.follow(toFollow);
        return getUsersFollowed(login);
    }

    public List<UserDTO> getFollowing(String login) throws UserNotFoundException {
        return getUsersFollowed(login);
    }

    public List<TweetDTO> getUserTimeline(String login) throws UserNotFoundException {
        return tweetsRepository.getUserTimeline(getUser(login))
                .stream()
                .map(TweetDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<TweetDTO> getUserWall(String login) throws UserNotFoundException {
        return tweetsRepository.getUserWall(getUser(login))
                .stream()
                .map(TweetDTO::toDTO)
                .collect(Collectors.toList());
    }

    private User getUser(String login) {
        return usersRepository.getUser(login)
                .orElseThrow(() -> new UserNotFoundException("Unable to find User with login: ".concat(login)));
    }

    private User getOrCreateNewUser(String login) {
        return usersRepository.getUser(login)
                .orElseGet(() -> usersRepository.createUser(login));
    }

    private List<UserDTO> getUsersFollowed(String login) {
        return getUser(login).getFollowed().stream()
                .map(UserDTO::toDTO)
                .collect(Collectors.toList());
    }

}