package com.goylik.questionsPortal.questionsPortal.model.service.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerOptionMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IQuestionMapper;
import com.goylik.questionsPortal.questionsPortal.model.repository.QuestionRepository;
import com.goylik.questionsPortal.questionsPortal.model.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;
    private final IQuestionMapper questionMapper;
    private final IAnswerOptionMapper optionMapper;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerService answerService,
                           IQuestionMapper questionMapper, IAnswerOptionMapper optionMapper) {
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.questionMapper = questionMapper;
        this.optionMapper = optionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDto> getIncomingQuestionsByUserId(Integer id, Pageable pageable) {
        Page<Question> questions = this.questionRepository.getIncomingQuestionsByUserId(id, pageable);
        List<QuestionDto> questionDtoList = questions.stream().map(this.questionMapper::mapToShow).toList();
        return new PageImpl<>(questionDtoList, pageable, questions.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDto> getOutgoingQuestionsByUserId(Integer id, Pageable pageable) {
        Page<Question> questions = this.questionRepository.getOutgoingQuestionsByUserId(id, pageable);
        List<QuestionDto> questionDtoList = questions.stream().map(this.questionMapper::mapToShow).toList();
        return new PageImpl<>(questionDtoList, pageable, questions.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerOptionDto> findAllAnswerOptions(Pageable pageable) {
        Page<AnswerOption> options = this.questionRepository.findAllAnswerOptions(pageable);
        List<AnswerOptionDto> optionDtoList = options.stream().map(this.optionMapper::map).toList();
        return new PageImpl<>(optionDtoList, pageable, options.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerOptionDto> findAllAnswerOptionsByQuestionId(Integer id) {
        List<AnswerOption> options = this.questionRepository.findAllAnswerOptionsByQuestionId(id);
        List<AnswerOptionDto> optionDtoList = options.stream().map(this.optionMapper::map).toList();
        return optionDtoList;
    }

    @Override
    @Transactional
    public void deleteAnswerOptionsByQuestionId(Integer id) {
        this.questionRepository.deleteAnswerOptionsByQuestionId(id);
    }

    @Override
    @Transactional
    public void update(QuestionDto questionDto) {
        if (questionDto.getAnswer() != null) {
            this.answerService.delete(questionDto.getAnswer());
            questionDto.setAnswer(null);
        }

        Question question = this.questionMapper.map(questionDto);
        if (question.getOptions() != null) {
            List<AnswerOption> options = question.getOptions();
            options = options.stream()
                    .filter(option -> !option.getOption().isEmpty())
                    .toList();

            question.setOptions(options);
        }

        this.questionRepository.save(question);
    }

    @Override
    @Transactional
    public QuestionDto save(QuestionDto questionDto) {
        Question question = this.questionMapper.map(questionDto);
        question = this.questionRepository.save(question);
        if (question.getOptions() != null) {
            List<AnswerOption> options = question.getOptions();
            options = options.stream()
                    .filter(option -> !option.getOption().isEmpty())
                    .toList();

            question.setOptions(options);
        }

        return this.questionMapper.map(question);
    }

    @Override
    @Transactional
    public void delete(QuestionDto questionDto) {
        Question question = this.questionMapper.map(questionDto);
        this.questionRepository.delete(question);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDto> findAll(Pageable pageable) {
        Page<Question> questions = this.questionRepository.findAll(pageable);
        List<QuestionDto> questionDtoList = questions.stream().map(this.questionMapper::map).toList();
        return new PageImpl<>(questionDtoList, pageable, questions.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDto findById(Integer id) {
        Question question = this.questionRepository.findById(id).orElse(null);
        QuestionDto questionDto = this.questionMapper.map(question);
        return questionDto;
    }
}
