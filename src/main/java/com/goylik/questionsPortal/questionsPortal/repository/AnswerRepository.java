package com.goylik.questionsPortal.questionsPortal.repository;

import com.goylik.questionsPortal.questionsPortal.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
