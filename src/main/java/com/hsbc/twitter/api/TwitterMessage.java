package com.hsbc.twitter.api;

import io.swagger.annotations.ApiModelProperty;

public class TwitterMessage {

    @ApiModelProperty(notes = "Tweeted message (max 140 characters)")
    public String message;

    public TwitterMessage() {
    }

    public TwitterMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
