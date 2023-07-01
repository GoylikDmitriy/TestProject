package com.goylik.questionsPortal.questionsPortal.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer extends BaseEntity {
    @Column(name = "answer")
    private String answer;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer() {}

    public Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                '}';
    }


}
