package com.goylik.questionsPortal.questionsPortal.model;

import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.repository.QuestionRepository;
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
public class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void shouldFindAllAnswerOptions() {
        List<AnswerOption> options = this.questionRepository.findAllAnswerOptions();
        assertThat(options).hasSize(3);
    }

    @Test
    public void shouldFindAnswerOptions() {
        Question question = this.questionRepository.findById(4).orElse(null);
        List<AnswerOption> options = question.getOptions();
        assertThat(options).hasSize(3);

        question = this.questionRepository.findById(1).orElse(null);
        options = question.getOptions();
        assertThat(options).isEmpty();
    }

    @Test
    public void shouldFindAllQuestions() {
        List<Question> questions = this.questionRepository.findAll();
        assertThat(questions).hasSize(4);
    }

    @Test
    public void shouldFindQuestionById() {
        Question question = this.questionRepository.findById(1).orElse(null);
        assertThat(question).isNotNull();

        question = this.questionRepository.findById(999).orElse(null);
        assertThat(question).isNull();
    }

    @Test
    @Transactional
    public void shouldInsertQuestion() {
        Question question = new Question("test question", AnswerType.SINGLE_LINE);
        this.questionRepository.save(question);
        List<Question> questions = this.questionRepository.findAll();
        assertThat(questions).hasSize(5);
        Question savedQuestion = questions.get(questions.size() - 1);
        assertThat(savedQuestion).isNotNull();
        assertThat(savedQuestion).isEqualTo(question);
    }

    @Test
    @Transactional
    public void shouldUpdateQuestion() {
        Question question = this.questionRepository.findById(1).orElse(null);
        question.setQuestion("test question");
        this.questionRepository.save(question);
        question = this.questionRepository.findById(1).orElse(null);
        assertThat(question.getQuestion()).isEqualTo("test question");
    }

    @Test
    @Transactional
    public void shouldDeleteQuestion() {
        Question question = this.questionRepository.findById(1).orElse(null);
        this.questionRepository.delete(question);
        List<Question> questions = this.questionRepository.findAll();
        question = this.questionRepository.findById(1).orElse(null);
        assertThat(questions).hasSize(3);
        assertThat(question).isNull();
    }
}
