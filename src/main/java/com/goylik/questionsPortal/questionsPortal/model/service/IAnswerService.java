package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;

import java.util.List;

public interface IAnswerService {
    List<AnswerDto> findAll();
    AnswerDto findById(Integer id);
    AnswerDto save(AnswerDto answer);
    void update(AnswerDto answer);
    void delete(AnswerDto answer);
}
