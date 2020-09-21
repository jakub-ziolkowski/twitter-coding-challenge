package com.hsbc.twitter.api;

import com.hsbc.twitter.api.dto.TweetDTO;
import com.hsbc.twitter.api.dto.UserDTO;
import com.hsbc.twitter.domain.TwitterService;
import com.hsbc.twitter.domain.tweet.boundary.TweetsRepository;
import com.hsbc.twitter.domain.tweet.entity.Tweet;
import com.hsbc.twitter.domain.tweet.exceptions.TweetTooLongException;
import com.hsbc.twitter.domain.user.boundary.UsersRepository;
import com.hsbc.twitter.domain.user.entity.User;
import com.hsbc.twitter.domain.user.exceptions.FollowingException;
import com.hsbc.twitter.domain.user.exceptions.UserNotFoundException;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(description = "REST API of Simple Social Networking")
public class TwitterRestController {

    private static final Logger log = LoggerFactory.getLogger(TwitterRestController.class);

    @Autowired
    public TwitterService twitterService;

    @ApiOperation(
            value = "Creates a new Tweet on behalf of the User",
            notes = "If the User with the given login does not exist, it will be automatically created",
            response = TweetDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created new Tweet"),
            @ApiResponse(code = 400, message = "There was a problem creating a Tweet - please follow exception status for details")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{login}/tweet", method = RequestMethod.PUT, consumes = "text/html", produces = "application/json")
    public @ResponseBody
    TweetDTO tweet(
            @ApiParam(value = "Login of Twitter User", required = true) @PathVariable("login") String login,
            @ApiParam(value = "Tweet message", required = true) @RequestBody String body
    ) {
        try {
            return twitterService.createTweet(login, body);
        } catch (TweetTooLongException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(
            value = "List Tweets of particular User in reverse chronological order",
            response = TweetDTO.class, responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The User's Tweet list was successfully returned"),
            @ApiResponse(code = 400, message = "There was a problem returning the User's Tweet list - please follow exception status for details")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{login}/wall", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<TweetDTO> showUserWall(
            @ApiParam(value = "Login of Twitter User", required = true) @PathVariable("login") String login
    ) {
        try {
            return twitterService.getUserWall(login);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(
            value = "Makes User to follow another User. Response contains list of all followed Users",
            response = UserDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully followed new User"),
            @ApiResponse(code = 400, message = "There was a problem following another User - please follow exception status for details")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{login}/follow/{another}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    List<UserDTO> follow(
            @ApiParam(value = "Login of Twitter User", required = true) @PathVariable("login") String login,
            @ApiParam(value = "Login of Twitter User to be followed", required = true) @PathVariable("another") String another
    ) {
        try {
            return twitterService.followUser(login, another);
        } catch (UserNotFoundException | FollowingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(
            value = "List of all followed Users",
            response = UserDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The User's followed list was successfully returned"),
            @ApiResponse(code = 400, message = "There was a problem returning the User's following list - please follow exception status for details")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{login}/following", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<UserDTO> followedList(
            @ApiParam(value = "Login of Twitter User", required = true) @PathVariable("login") String login
    ) {
        try {
            return twitterService.getFollowing(login);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(
            value = "List Tweets posted by all the Users which User follow in reverse chronological order",
            response = TweetDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Tweet list posted by all the Users which User follow was successfully returned"),
            @ApiResponse(code = 400, message = "There was a problem returning the User's Timeline - please follow exception status for details")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{login}/timeline", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<TweetDTO> showUserTimeline(
            @ApiParam(value = "Login of Twitter User", required = true) @PathVariable("login") String login
    ) {
        try {
            return twitterService.getUserTimeline(login);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
