package com.hsbc.twitter.api;

import com.hsbc.twitter.api.dto.TweetDTO;
import com.hsbc.twitter.api.dto.UserDTO;
import com.hsbc.twitter.domain.tweet.boundary.TweetsRepository;
import com.hsbc.twitter.domain.tweet.entity.Tweet;
import com.hsbc.twitter.domain.user.boundary.UsersRepository;
import com.hsbc.twitter.domain.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TwitterRestControllerUnitTest {

    private static final Logger log = LoggerFactory.getLogger(TwitterRestControllerUnitTest.class);

    @Autowired
    private TwitterRestController twitterRestController;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TweetsRepository tweetsRepository;

    @BeforeEach
    void resetRepositories(){
        tweetsRepository.reset();
        usersRepository.reset();
    }
    @Test
    void test_Tweet_And_User_Should_Be_Created_In_Repository() {
        TweetDTO tweet = twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));

        Optional<Tweet> created_tweet = tweetsRepository.getTweet(tweet.getId());
        assertTrue(created_tweet.isPresent());

        Optional<User> created_user = usersRepository.getUser("TESTER");
        assertTrue(created_user.isPresent());
    }

    @Test()
    void test_Tweet_And_User_Should_NOT_Be_Created_In_Repository_And_Exception_Should_Be_Thrown() {
        Exception e = assertThrows(ResponseStatusException.class, () ->
                twitterRestController.tweet("TESTER", new TwitterMessage(RandomStringUtils.randomAlphabetic(141)))
        );

        assertFalse(usersRepository.getUser("TESTER").isPresent());

        String expectedMessage = "400 BAD_REQUEST \"The message length has exceeded the maximum size limit of 140 characters\"";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void test_User_Should_Be_Able_To_Follow_Another_User() {
        twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));
        twitterRestController.tweet("DEVELOPER", new TwitterMessage("Another valid message for test"));

        List<UserDTO> followed = twitterRestController.follow("TESTER", "DEVELOPER");

        assertEquals(followed.size(), 1);
        assertEquals(followed.get(0).getLogin(), "DEVELOPER");
    }

    @Test
    void test_User_Should_NOT_Be_Able_To_Follow_Non_Existent_User() {
        twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));

        Exception e = assertThrows(ResponseStatusException.class, () ->
                twitterRestController.follow("TESTER", "NON_EXISTENT_USER")
        );

        String expectedMessage = "400 BAD_REQUEST \"Unable to find User with login: NON_EXISTENT_USER\"";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void test_User_Should_Be_Able_To_See_Followed_Users() {
        twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));
        twitterRestController.tweet("DEVELOPER", new TwitterMessage("Another valid message for test"));
        twitterRestController.tweet("ADMIN", new TwitterMessage("Yet another valid message for test"));

        twitterRestController.follow("TESTER", "DEVELOPER");
        twitterRestController.follow("TESTER", "ADMIN");

        UserDTO developer = usersRepository.getUser("DEVELOPER").map(UserDTO::toDTO).get();
        UserDTO admin = usersRepository.getUser("ADMIN").map(UserDTO::toDTO).get();

        List<UserDTO> tester_followed_list = twitterRestController.followedList("TESTER");

        assertTrue(tester_followed_list.contains(developer));
        assertTrue(tester_followed_list.contains(admin));

        assertEquals(tester_followed_list.size(), 2);
    }

    @Test()
    void test_User_Should_NOT_Be_Able_To_Follow_Himself_And_Exception_Should_Be_Thrown() {
        twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));
        Exception e = assertThrows(ResponseStatusException.class, () ->
                twitterRestController.follow("TESTER", "TESTER")
        );

        String expectedMessage = "400 BAD_REQUEST \"You cant follow yourself\"";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test()
    void test_User_Should_Be_Able_To_See_His_Wall_Posts_Ordered_By_Creation_Date_Descending(){
        twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));
        twitterRestController.tweet("TESTER", new TwitterMessage("Another valid message for test"));
        twitterRestController.tweet("TESTER", new TwitterMessage("Yet another valid message for test"));

        Optional<User> tester = usersRepository.getUser("TESTER");
        List<Tweet> userWall = tweetsRepository.getUserWall(tester.get());

        // List should have 3 Tweets
        assertEquals(userWall.size(), 3);

        // create copy of list and sort it
        List<Tweet> userWallSorted = new LinkedList<>(userWall);
        Collections.sort(userWallSorted, Comparator.comparing(Tweet::getCreated).reversed());

        // List should be in same order
        assertTrue(userWall.equals(userWallSorted));
    }

    @Test
    void test_User_Should_Be_Able_To_See_His_Timeline_Ordered_By_Creation_Date_Descending(){
        twitterRestController.tweet("TESTER", new TwitterMessage("Valid message for test"));
        twitterRestController.tweet("ADMIN", new TwitterMessage("Another valid message for test"));
        twitterRestController.tweet("ADMIN", new TwitterMessage("Yet yey yet another valid message for test"));
        twitterRestController.tweet("DEVELOPER", new TwitterMessage("Yet another valid message for test"));
        twitterRestController.tweet("ADMIN", new TwitterMessage("Yet yet another valid message for test"));

        twitterRestController.follow("TESTER", "ADMIN");
        twitterRestController.follow("TESTER", "DEVELOPER");

        Optional<User> tester = usersRepository.getUser("TESTER");
        List<Tweet> userTimeline = tweetsRepository.getUserTimeline(tester.get());

        // List should have 3 Tweets
        assertEquals(userTimeline.size(), 4);

        // create copy of list and sort it
        List<Tweet> userTimelineSorted = new LinkedList<>(userTimeline);
        Collections.sort(userTimelineSorted, Comparator.comparing(Tweet::getCreated).reversed());

        // List should be in same order
        assertTrue(userTimeline.equals(userTimelineSorted));
    }

}