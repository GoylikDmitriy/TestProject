package com.goylik.questionsPortal.questionsPortal.model.dto;

import com.goylik.questionsPortal.questionsPortal.model.AnswerType;

import java.util.ArrayList;
import java.util.List;

public class QuestionDto {
    private Integer id;
    private String question;
    private AnswerType answerType;
    private AnswerDto answer;
    private List<AnswerOptionDto> options;
    private UserDto toUser;
    private UserDto fromUser;

    public UserDto getToUser() {
        return toUser;
    }

    public void setToUser(UserDto toUser) {
        this.toUser = toUser;
    }

    public UserDto getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserDto fromUser) {
        this.fromUser = fromUser;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public AnswerDto getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDto answer) {
        this.answer = answer;
    }

    public List<AnswerOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOptionDto> options) {
        this.options = options;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String answerString = "";
        String emailFrom = "";
        String emailTo = "";
        List<AnswerOptionDto> options = new ArrayList<>();
        if(this.answer != null) {
            answerString = this.answer.getAnswer();
        }
        if (this.fromUser != null) {
            emailFrom = this.fromUser.getEmail();
        }

        if (this.toUser != null) {
            emailTo = this.toUser.getEmail();
        }

        if (this.options != null) {
            options = this.options;
        }

        return "QuestionDto{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerType=" + answerType +
                ", answer=" + answerString +
                ", options=" + options +
                ", toUser=" + emailTo +
                ", fromUser=" + emailFrom +
                '}';
    }
}
