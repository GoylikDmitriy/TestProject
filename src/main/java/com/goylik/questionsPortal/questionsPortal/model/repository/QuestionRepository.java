package com.goylik.questionsPortal.questionsPortal.model.repository;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("SELECT option FROM AnswerOption")
    List<AnswerOption> findAllAnswerOptions();

    @Query("SELECT option FROM AnswerOption")
    Page<AnswerOption> findAllAnswerOptions(Pageable pageable);

    @Query("SELECT options FROM Question question WHERE question.id = :id")
    List<AnswerOption> findAllAnswerOptionsByQuestionId(@Param("id") Integer id);

    @Modifying
    @Query("DELETE FROM AnswerOption option WHERE option IN " +
            "(SELECT options FROM Question question WHERE question.id=:id)")
    void deleteAnswerOptionsByQuestionId(@Param("id") Integer id);

    Page<Question> findAll(Pageable pageable);
}
