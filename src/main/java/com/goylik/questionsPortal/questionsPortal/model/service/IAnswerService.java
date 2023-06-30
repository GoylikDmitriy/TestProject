package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAnswerService {
    Page<AnswerDto> findAll(Pageable pageable);
    AnswerDto findById(Integer id);
    AnswerDto save(AnswerDto answer);
    void update(AnswerDto answer);
    void delete(AnswerDto answer);
}
