package com.goylik.questionsPortal.questionsPortal.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_options")
public class AnswerOption extends BaseEntity {
    @Column(name = "option")
    private String option;

    public AnswerOption() {}

    public AnswerOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "AnswerOption{" +
                "option='" + option + '\'' +
                '}';
    }
}
