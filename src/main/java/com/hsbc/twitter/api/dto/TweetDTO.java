package com.hsbc.twitter.api.dto;

import com.hsbc.twitter.domain.tweet.entity.Tweet;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class TweetDTO {

    @ApiModelProperty(notes = "UUID assigned to Tweet")
    private UUID id;

    @ApiModelProperty(notes = "The User to whom the Tweet belongs")
    private UserDTO user;

    @ApiModelProperty(notes = "Tweeted message")
    private String message;

    @ApiModelProperty(notes = "When Tweet was created")
    private LocalDateTime created;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (this == object) return true;
        return Objects.equals(id, ((TweetDTO)object).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static TweetDTO toDTO(Tweet tweet){
        TweetDTO result = new TweetDTO();
        result.setId(tweet.getId());
        result.setCreated(tweet.getCreated());
        result.setUser(UserDTO.toDTO(tweet.getUser()));
        result.setMessage(tweet.getMessage());
        return result;
    }

}
