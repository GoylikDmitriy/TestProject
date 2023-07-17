package com.goylik.questionsPortal.questionsPortal.model.dto.token;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;

import java.sql.Date;

public class VerificationTokenDto {
    private Integer id;
    private String token;
    private UserDto user;
    private Date expiryDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
