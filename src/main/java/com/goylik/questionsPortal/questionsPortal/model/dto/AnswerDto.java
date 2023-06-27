package com.goylik.questionsPortal.questionsPortal.model.dto;

public class AnswerDto {
    private String answer;
    private QuestionDto question;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionDto getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDto question) {
        this.question = question;
    }
}