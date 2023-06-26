package com.goylik.questionsPortal.questionsPortal.model.entity;

import com.goylik.questionsPortal.questionsPortal.model.AnswerType;
import com.goylik.questionsPortal.questionsPortal.model.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "questions")
public class Question extends BaseEntity {
    @Column(name = "question")
    private String question;

    @Column(name = "answer_type")
    @Enumerated(value = EnumType.STRING)
    private AnswerType answerType;

    @OneToOne(mappedBy = "question", cascade = CascadeType.REMOVE)
    private Answer answer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private List<AnswerOption> options;

    public Question() {}

    public Question(String question, AnswerType answerType) {
        this.question = question;
        this.answerType = answerType;
    }

    public void addAnswerOption(AnswerOption option) {
        if (option.isNew()) {
            if (this.options == null) {
                this.options = new ArrayList<>();
            }

            this.options.add(option);
        }
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

    public List<AnswerOption> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOption> given_answers) {
        this.options = given_answers;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answerType=" + answerType +
                ", answer=" + answer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return Objects.equals(question, question1.question) && answerType == question1.answerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answerType);
    }
}