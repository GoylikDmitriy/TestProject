package com.goylik.questionsPortal.questionsPortal.service;

import com.goylik.questionsPortal.questionsPortal.model.Answer;
import com.goylik.questionsPortal.questionsPortal.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Transactional
    public void save(Answer answer) {
        this.answerRepository.save(answer);
    }

    @Transactional
    public void update(Answer answer) {
        this.answerRepository.save(answer);
    }

    @Transactional
    public void delete(Answer answer) {
        answer.getQuestion().setAnswer(null);
        this.answerRepository.delete(answer);
    }

    @Transactional(readOnly = true)
    public List<Answer> findAll() {
        return this.answerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Answer findById(int id) {
        return this.answerRepository.findById(id).orElse(null);
    }
}
