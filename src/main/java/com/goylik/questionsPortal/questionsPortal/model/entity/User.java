package com.goylik.questionsPortal.questionsPortal.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Question> incomingQuestions;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Question> outgoingQuestions;

    @Column(name = "enabled")
    private Boolean enabled;

    public User() {
        this.enabled = Boolean.FALSE;
    }

    public User(String firstName, String lastName, String email, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.enabled = Boolean.FALSE;
    }

    public void addOutgoingQuestion(Question question) {
        if (question.isNew()) {
            if (this.outgoingQuestions == null) {
                this.outgoingQuestions = new ArrayList<>();
            }

            this.outgoingQuestions.add(question);
        }
    }

    public void addIncomingQuestion(Question question) {
        if (question.isNew()) {
            if (this.incomingQuestions == null) {
                this.incomingQuestions = new ArrayList<>();
            }

            this.incomingQuestions.add(question);
        }
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Question> getIncomingQuestions() {
        return incomingQuestions;
    }

    public void setIncomingQuestions(List<Question> incomingQuestions) {
        this.incomingQuestions = incomingQuestions;
    }

    public List<Question> getOutgoingQuestions() {
        return outgoingQuestions;
    }

    public void setOutgoingQuestions(List<Question> outgoingQuestions) {
        this.outgoingQuestions = outgoingQuestions;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phoneNumber);
    }
}
