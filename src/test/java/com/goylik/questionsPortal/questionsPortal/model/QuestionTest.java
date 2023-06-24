package com.goylik.questionsPortal.questionsPortal.model;

import com.goylik.questionsPortal.questionsPortal.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    @Autowired
    private QuestionService questionService;

    @Test
    public void shouldFindAnswerOptions() {
        Question question = this.questionService.findById(4);
        List<AnswerOption> options = question.getOptions();
        assertThat(options).hasSize(3);

        question = this.questionService.findById(1);
        options = question.getOptions();
        assertThat(options).isEmpty();
    }

    @Test
    public void shouldFindAllQuestions() {
        List<Question> questions = this.questionService.findAll();
        assertThat(questions).hasSize(4);
    }

    @Test
    public void shouldFindQuestionById() {
        Question question = this.questionService.findById(1);
        assertThat(question).isNotNull();

        question = this.questionService.findById(999);
        assertThat(question).isNull();
    }

    @Test
    public void shouldInsertQuestion() {
        Question question = new Question("test question", AnswerType.SINGLE_LINE);
        this.questionService.save(question);
        List<Question> questions = this.questionService.findAll();
        assertThat(questions).hasSize(5);
        Question savedQuestion = questions.get(questions.size() - 1);
        assertThat(savedQuestion).isNotNull();
        assertThat(savedQuestion).isEqualTo(question);
    }

    @Test
    public void shouldUpdateQuestion() {
        Question question = this.questionService.findById(1);
        question.setQuestion("test question");
        this.questionService.update(question);
        question = this.questionService.findById(1);
        assertThat(question.getQuestion()).isEqualTo("test question");
    }

    @Test
    public void shouldDeleteQuestion() {
        Question question = this.questionService.findById(1);
        this.questionService.delete(question);
        List<Question> questions = this.questionService.findAll();
        question = this.questionService.findById(1);
        assertThat(questions).hasSize(3);
        assertThat(question).isNull();
    }
}
