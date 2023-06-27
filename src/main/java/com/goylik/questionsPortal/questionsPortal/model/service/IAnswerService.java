package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;

import java.util.List;

public interface IAnswerService {
    List<Answer> findAll();
    Answer findById(Integer id);
    void save(Answer answer);
    void update(Answer answer);
    void delete(Answer answer);
}
