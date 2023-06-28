package com.goylik.questionsPortal.questionsPortal.model.service.impl;

import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.repository.AnswerRepository;
import com.goylik.questionsPortal.questionsPortal.model.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService implements IAnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public void save(Answer answer) {
        this.answerRepository.save(answer);
    }

    @Override
    @Transactional
    public void update(Answer answer) {
        this.answerRepository.save(answer);
    }

    @Override
    @Transactional
    public void delete(Answer answer) {
        answer.getQuestion().setAnswer(null);
        this.answerRepository.delete(answer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Answer> findAll() {
        return this.answerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Answer findById(Integer id) {
        return this.answerRepository.findById(id).orElse(null);
    }
}
