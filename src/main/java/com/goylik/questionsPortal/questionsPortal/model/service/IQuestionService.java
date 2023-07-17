package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IQuestionService {
    Page<QuestionDto> findAll(Pageable pageable);
    QuestionDto findById(Integer id);
    QuestionDto save(QuestionDto question);
    QuestionDto update(QuestionDto question);
    void delete(QuestionDto question);
    void deleteAnswerOptionsByQuestionId(Integer id);
    Page<AnswerOptionDto> findAllAnswerOptions(Pageable pageable);
    List<AnswerOptionDto> findAllAnswerOptionsByQuestionId(Integer id);

    Page<QuestionDto> getIncomingQuestionsByUserId(Integer id, Pageable pageable);
    Page<QuestionDto> getOutgoingQuestionsByUserId(Integer id, Pageable pageable);
}
