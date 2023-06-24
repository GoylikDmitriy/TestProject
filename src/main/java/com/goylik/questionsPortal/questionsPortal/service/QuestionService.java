package com.goylik.questionsPortal.questionsPortal.service;

import com.goylik.questionsPortal.questionsPortal.model.Question;
import com.goylik.questionsPortal.questionsPortal.repository.QuestionRepository;
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

    @Transactional
    public void update(Question question) {
        this.answerService.delete(question.getAnswer());
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
