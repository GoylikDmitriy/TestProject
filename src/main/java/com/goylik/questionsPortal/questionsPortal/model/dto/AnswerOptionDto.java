package com.goylik.questionsPortal.questionsPortal.model.dto;

public class AnswerOptionDto {
    private Integer id;
    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
