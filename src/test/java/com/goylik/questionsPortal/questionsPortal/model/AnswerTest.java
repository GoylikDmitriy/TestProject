package com.goylik.questionsPortal.questionsPortal.model;

import com.goylik.questionsPortal.questionsPortal.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerService answerService;

    @Test
    public void shouldFindAllAnswers() {
        List<Answer> answers = this.answerService.findAll();
        assertThat(answers).hasSize(3);
    }

    @Test
    public void shouldFindAnswerById() {
        Answer answer = this.answerService.findById(1);
        assertThat(answer).isNotNull();

        answer = this.answerService.findById(999);
        assertThat(answer).isNull();
    }

    @Test
    public void shouldInsertAnswer() {
        Answer answer = new Answer("yes.");
        this.answerService.save(answer);
        List<Answer> answers = this.answerService.findAll();
        assertThat(answers).hasSize(4);
        Answer savedAnswer = answers.get(answers.size() - 1);
        assertThat(savedAnswer).isNotNull();
        assertThat(savedAnswer).isEqualTo(answer);
    }

    @Test
    public void shouldUpdateAnswer() {
        Answer answer = this.answerService.findById(1);
        answer.setAnswer("changed.");
        this.answerService.update(answer);
        answer = this.answerService.findById(1);
        assertThat(answer.getAnswer()).isEqualTo("changed.");
    }

    @Test
    public void shouldDeleteAnswer() {
        Answer answer = this.answerService.findById(1);
        this.answerService.delete(answer);
        List<Answer> answers = this.answerService.findAll();
        answer = this.answerService.findById(1);
        assertThat(answers).hasSize(2);
        assertThat(answer).isNull();
    }
}
