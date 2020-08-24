package com.hsbc.twitter.api.dto;

import com.hsbc.twitter.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class UserDTO {

    @ApiModelProperty(notes = "Login of Twitter user")
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (this == object) return true;
        return Objects.equals(login, ((UserDTO)object).login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    public static UserDTO toDTO(User user){
        UserDTO result = new UserDTO();
        result.setLogin(user.getLogin());
        return result;
    }

}
