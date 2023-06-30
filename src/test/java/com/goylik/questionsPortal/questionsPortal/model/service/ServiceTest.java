package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.*;
import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceTest extends DataJpaTest {

    @Autowired
    private IUserService userService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IAnswerService answerService;

    Pageable pageable;
    @BeforeEach
    public void setup() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE);
    }

    @Test
    public void whenDeleteUserDeleteQuestions() {
        UserDto user = this.userService.findById(1);
        this.userService.delete(user);
        Page<QuestionDto> questions = this.questionService.findAll(pageable);

        assertThat(questions).hasSize(2);
        assertThat(this.questionService.findById(1)).isNull();
        assertThat(this.questionService.findById(4)).isNull();
    }

    @Test
    public void whenUpdateQuestionDeleteAnswer() {
        QuestionDto question = this.questionService.findById(1);
        question.setQuestion("TEST change.");
        this.questionService.update(question);
        Page<AnswerDto> answers = this.answerService.findAll(pageable);

        assertThat(answers).hasSize(2);
        assertThat(this.answerService.findById(1)).isNull();
    }

    @Test
    public void whenDeleteQuestionDeleteAnswer() {
        QuestionDto question = this.questionService.findById(1);
        this.questionService.delete(question);
        Page<AnswerDto> answers = this.answerService.findAll(pageable);

        assertThat(answers).hasSize(2);
        assertThat(this.answerService.findById(1)).isNull();
    }

    @Test
    public void whenDeleteAnswerQuestionAnswerIsNull() {
        AnswerDto answer = this.answerService.findById(1);
        this.answerService.delete(answer);
        Page<QuestionDto> questions = this.questionService.findAll(pageable);
        QuestionDto question = this.questionService.findById(1);

        assertThat(questions).hasSize(4);
        assertThat(question.getAnswer()).isNull();
    }

    @Test
    public void whenDeleteUserDeleteAnswers() {
        UserDto user = this.userService.findById(1);
        this.userService.delete(user);
        Page<AnswerDto> answers = this.answerService.findAll(pageable);
        assertThat(answers).hasSize(1);
    }

    @Test
    public void whenUpdateQuestionWithOptionsDeleteOptions() {
        QuestionDto question = this.questionService.findById(4);
        question.setQuestion("Hello!");
        question.setAnswerType(AnswerType.SINGLE_LINE);
        this.questionService.update(question);

        question = this.questionService.findById(4);
        List<AnswerOptionDto> options = question.getOptions();
        assertThat(options).isNull();

        Page<AnswerOptionDto> answerOptions = this.questionService.findAllAnswerOptions(pageable);
        assertThat(answerOptions).isEmpty();
    }
}