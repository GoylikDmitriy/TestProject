package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private AnswerService answerService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Autowired
    public void setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Transactional(readOnly = true)
    public List<AnswerOption> findAllAnswerOptions() {
        return this.questionRepository.findAllAnswerOptions();
    }

    @Transactional(readOnly = true)
    public List<AnswerOption> findAllAnswerOptionsByQuestionId(Integer id) {
        return this.questionRepository.findAllAnswerOptionsByQuestionId(id);
    }

    @Transactional
    public void deleteAnswerOptionsByQuestionId(Integer id) {
        this.questionRepository.deleteAnswerOptionsByQuestionId(id);
    }

    @Transactional
    public void update(Question question) {
        if (question.getAnswer() != null) {
            this.answerService.delete(question.getAnswer());
        }

        if (question.getOptions() != null) {
            this.questionRepository.deleteAnswerOptionsByQuestionId(question.getId());
            question.setOptions(null);
        }

        this.questionRepository.save(question);
    }

    @Transactional
    public void save(Question question) {
        this.questionRepository.save(question);
    }

    @Transactional
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return this.questionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Question findById(int id) {
        return this.questionRepository.findById(id).orElse(null);
    }
}
