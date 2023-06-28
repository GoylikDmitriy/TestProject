package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;

import java.util.List;

public interface IQuestionService {
    List<Question> findAll();
    Question findById(Integer id);
    void save(Question question);
    void update(Question question);
    void delete(Question question);
    void deleteAnswerOptionsByQuestionId(Integer id);
    List<AnswerOption> findAllAnswerOptionsByQuestionId(Integer id);
    List<AnswerOption> findAllAnswerOptions();
}
