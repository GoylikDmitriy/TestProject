package com.goylik.questionsPortal.questionsPortal.event;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private UserDto userDto;

    public OnRegistrationCompleteEvent(UserDto userDto, String appUrl) {
        super(userDto);
        this.userDto = userDto;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public UserDto getUser() {
        return userDto;
    }

    public void setUser(UserDto userDto) {
        this.userDto = userDto;
    }
}