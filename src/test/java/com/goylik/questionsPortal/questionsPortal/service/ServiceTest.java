package com.goylik.questionsPortal.questionsPortal.service;

import com.goylik.questionsPortal.questionsPortal.model.*;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.service.AnswerService;
import com.goylik.questionsPortal.questionsPortal.model.service.QuestionService;
import com.goylik.questionsPortal.questionsPortal.model.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TypeExcludeFilters({org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter.class})
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
public class ServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;

    @Test
    public void whenDeleteUserDeleteQuestions() {
        User user = this.userService.findById(1);
        this.userService.delete(user);
        List<Question> questions = this.questionService.findAll();

        assertThat(questions).hasSize(2);
        assertThat(this.questionService.findById(1)).isNull();
        assertThat(this.questionService.findById(4)).isNull();
    }

    @Test
    public void whenUpdateQuestionDeleteAnswer() {
        Question question = this.questionService.findById(1);
        question.setQuestion("TEST change.");
        this.questionService.update(question);
        List<Answer> answers = this.answerService.findAll();

        assertThat(answers).hasSize(2);
        assertThat(this.answerService.findById(1)).isNull();
    }

    @Test
    public void whenDeleteQuestionDeleteAnswer() {
        Question question = this.questionService.findById(1);
        this.questionService.delete(question);
        List<Answer> answers = this.answerService.findAll();

        assertThat(answers).hasSize(2);
        assertThat(this.answerService.findById(1)).isNull();
    }

    @Test
    public void whenDeleteAnswerQuestionAnswerIsNull() {
        Answer answer = this.answerService.findById(1);
        this.answerService.delete(answer);
        List<Question> questions = this.questionService.findAll();
        Question question = this.questionService.findById(1);

        assertThat(questions).hasSize(4);
        assertThat(question.getAnswer()).isNull();
    }

    @Test
    public void whenDeleteUserDeleteAnswers() {
        User user = this.userService.findById(1);
        this.userService.delete(user);
        List<Answer> answers = this.answerService.findAll();
        assertThat(answers).hasSize(1);
    }

    @Test
    public void whenUpdateQuestionWithOptionsDeleteOptions() {
        Question question = this.questionService.findById(4);
        question.setQuestion("Hello!");
        question.setAnswerType(AnswerType.SINGLE_LINE);
        this.questionService.update(question);

        question = this.questionService.findById(4);
        List<AnswerOption> options = question.getOptions();
        assertThat(options).isNull();

        options = this.questionService.findAllAnswerOptions();
        assertThat(options).isEmpty();
    }
}