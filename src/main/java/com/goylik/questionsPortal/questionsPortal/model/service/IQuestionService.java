package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;

import java.util.List;

public interface IQuestionService {
    List<QuestionDto> findAll();
    QuestionDto findById(Integer id);
    QuestionDto save(QuestionDto question);
    void update(QuestionDto question);
    void delete(QuestionDto question);
    void deleteAnswerOptionsByQuestionId(Integer id);
    List<AnswerOptionDto> findAllAnswerOptionsByQuestionId(Integer id);
    List<AnswerOptionDto> findAllAnswerOptions();
}
