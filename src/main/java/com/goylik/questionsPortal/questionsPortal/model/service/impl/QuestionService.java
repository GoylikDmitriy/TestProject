package com.goylik.questionsPortal.questionsPortal.model.service.impl;

import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.repository.QuestionRepository;
import com.goylik.questionsPortal.questionsPortal.model.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService implements IQuestionService {
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

    @Override
    @Transactional(readOnly = true)
    public List<AnswerOption> findAllAnswerOptions() {
        return this.questionRepository.findAllAnswerOptions();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerOption> findAllAnswerOptionsByQuestionId(Integer id) {
        return this.questionRepository.findAllAnswerOptionsByQuestionId(id);
    }

    @Override
    @Transactional
    public void deleteAnswerOptionsByQuestionId(Integer id) {
        this.questionRepository.deleteAnswerOptionsByQuestionId(id);
    }

    @Override
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

    @Override
    @Transactional
    public void save(Question question) {
        this.questionRepository.save(question);
    }

    @Override
    @Transactional
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return this.questionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Question findById(Integer id) {
        return this.questionRepository.findById(id).orElse(null);
    }
}
