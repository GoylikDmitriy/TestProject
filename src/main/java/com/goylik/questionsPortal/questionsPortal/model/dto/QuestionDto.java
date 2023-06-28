package com.goylik.questionsPortal.questionsPortal.model.dto;

import com.goylik.questionsPortal.questionsPortal.model.AnswerType;

import java.util.List;

public class QuestionDto {
    private String question;
    private AnswerType answerType;
    private AnswerDto answer;
    private List<AnswerOptionDto> options;

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
}
