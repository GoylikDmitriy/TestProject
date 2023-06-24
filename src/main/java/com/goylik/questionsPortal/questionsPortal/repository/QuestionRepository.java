package com.goylik.questionsPortal.questionsPortal.repository;

import com.goylik.questionsPortal.questionsPortal.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
