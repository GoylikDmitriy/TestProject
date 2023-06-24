package com.goylik.questionsPortal.questionsPortal.service;

import com.goylik.questionsPortal.questionsPortal.model.Answer;
import com.goylik.questionsPortal.questionsPortal.model.Question;
import com.goylik.questionsPortal.questionsPortal.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
}