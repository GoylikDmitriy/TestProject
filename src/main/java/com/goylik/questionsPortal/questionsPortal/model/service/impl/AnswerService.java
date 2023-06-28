package com.goylik.questionsPortal.questionsPortal.model.service.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerMapper;
import com.goylik.questionsPortal.questionsPortal.model.repository.AnswerRepository;
import com.goylik.questionsPortal.questionsPortal.model.repository.QuestionRepository;
import com.goylik.questionsPortal.questionsPortal.model.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService implements IAnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final IAnswerMapper answerMapper;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, IAnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    @Transactional
    public AnswerDto save(AnswerDto answerDto) {
        Answer answer = this.answerMapper.map(answerDto);
        answer = this.answerRepository.save(answer);
        return this.answerMapper.map(answer);
    }

    @Override
    @Transactional
    public void update(AnswerDto answerDto) {
        Answer answer = this.answerMapper.map(answerDto);
        this.answerRepository.save(answer);
    }

    @Override
    @Transactional
    public void delete(AnswerDto answerDto) {
        Answer answer = this.answerMapper.map(answerDto);
        Question question = answer.getQuestion();
        question.setAnswer(null);
        this.questionRepository.save(question);
        this.answerRepository.delete(answer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerDto> findAll() {
        List<Answer> answers = this.answerRepository.findAll();
        List<AnswerDto> answerDtoList = answers.stream().map(this.answerMapper::map).toList();
        return answerDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerDto findById(Integer id) {
        Answer answer = this.answerRepository.findById(id).orElse(null);
        AnswerDto answerDto = this.answerMapper.map(answer);
        return answerDto;
    }
}
