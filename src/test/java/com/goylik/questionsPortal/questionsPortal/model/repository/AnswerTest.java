package com.goylik.questionsPortal.questionsPortal.model.repository;

import com.goylik.questionsPortal.questionsPortal.model.DataJpaTest;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest extends DataJpaTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void shouldFindAllAnswers() {
        List<Answer> answers = this.answerRepository.findAll();
        assertThat(answers).hasSize(3);
    }

    @Test
    public void shouldFindAnswerById() {
        Answer answer = this.answerRepository.findById(1).orElse(null);
        assertThat(answer).isNotNull();

        answer = this.answerRepository.findById(999).orElse(null);
        assertThat(answer).isNull();
    }

    @Test
    @Transactional
    public void shouldInsertAnswer() {
        Answer answer = new Answer("yes.");
        this.answerRepository.save(answer);
        List<Answer> answers = this.answerRepository.findAll();
        assertThat(answers).hasSize(4);
        Answer savedAnswer = answers.get(answers.size() - 1);
        assertThat(savedAnswer).isNotNull();
        assertThat(savedAnswer).isEqualTo(answer);
    }

    @Test
    @Transactional
    public void shouldUpdateAnswer() {
        Answer answer = this.answerRepository.findById(1).orElse(null);
        answer.setAnswer("changed.");
        this.answerRepository.save(answer);
        answer = this.answerRepository.findById(1).orElse(null);
        assertThat(answer.getAnswer()).isEqualTo("changed.");
    }

    @Test
    @Transactional
    public void shouldDeleteAnswer() {
        Answer answer = this.answerRepository.findById(1).orElse(null);
        this.answerRepository.delete(answer);
        List<Answer> answers = this.answerRepository.findAll();
        answer = this.answerRepository.findById(1).orElse(null);
        assertThat(answers).hasSize(2);
        assertThat(answer).isNull();
    }
}
