package com.goylik.questionsPortal.questionsPortal.model.repository;

import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findAll(Pageable pageable);

    @Query("SELECT options FROM Answer answer WHERE answer.id = :id")
    List<AnswerOption> findAnswerOptionsByAnswerId(Integer id);
}
