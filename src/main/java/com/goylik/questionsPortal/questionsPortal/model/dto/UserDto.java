package com.goylik.questionsPortal.questionsPortal.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDto {
    @NotEmpty(message = "shouldn't be empty")
    @Email(message = "email not valid")
    private String email;

    @Size(min = 5, message = "it should contain at least 5 symbols")
    private String password;
    private String confirmedPassword;

    @NotEmpty(message = "shouldn't be empty")
    @Size(max = 30, message = "max size is 30")
    private String firstName;

    @NotEmpty(message = "shouldn't be empty")
    @Size(max = 30, message = "max size is 30")
    private String lastName;

    @NotEmpty(message = "shouldn't be empty")
    @Digits(fraction = 0, integer = 9)
    private String phoneNumber;

    private List<QuestionDto> incomingQuestions;
    private List<QuestionDto> outgoingQuestions;

    public List<QuestionDto> getIncomingQuestions() {
        return incomingQuestions;
    }

    public void setIncomingQuestions(List<QuestionDto> incomingQuestions) {
        this.incomingQuestions = incomingQuestions;
    }

    public List<QuestionDto> getOutgoingQuestions() {
        return outgoingQuestions;
    }

    public void setOutgoingQuestions(List<QuestionDto> outgoingQuestions) {
        this.outgoingQuestions = outgoingQuestions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }
}