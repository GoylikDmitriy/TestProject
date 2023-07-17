package com.goylik.questionsPortal.questionsPortal.model.mapper.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerOptionMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnswerMapper implements IAnswerMapper {
    private IQuestionMapper questionMapper;
    private IAnswerOptionMapper optionMapper;

    @Override
    public AnswerDto mapToShow(AnswerDto dto) {
        AnswerDto answerDto = null;
        if (dto != null) {
            answerDto = new AnswerDto();
            answerDto.setId(dto.getId());
            answerDto.setAnswer(dto.getAnswer());
            answerDto.setOptions(dto.getOptions());
            answerDto.setQuestion(this.questionMapper
                    .mapToShow(this.questionMapper
                            .map(dto.getQuestion())
                    ));
        }

        return answerDto;
    }

    @Override
    public AnswerDto map(Answer entity) {
        AnswerDto answerDto = null;
        if (entity != null) {
            answerDto = new AnswerDto();
            answerDto.setId(entity.getId());
            answerDto.setAnswer(entity.getAnswer());
            answerDto.setOptions(this.convertOptions(entity.getOptions()));
            answerDto.setQuestion(this.questionMapper.map(entity.getQuestion()));
        }

        return answerDto;
    }

    @Override
    public Answer map(AnswerDto dto) {
        Answer answer = null;
        if (dto != null) {
            answer = new Answer();
            answer.setId(dto.getId());
            answer.setAnswer(dto.getAnswer());
            answer.setOptions(this.convertOptionDtoList(dto.getOptions()));
            answer.setQuestion(this.questionMapper.map(dto.getQuestion()));
        }

        return answer;
    }

    private List<AnswerOptionDto> convertOptions(List<AnswerOption> options) {
        List<AnswerOptionDto> optionDtoList = null;
        if (options != null) {
            optionDtoList = options.stream().map(this.optionMapper::map).toList();
        }

        return optionDtoList;
    }

    private List<AnswerOption> convertOptionDtoList(List<AnswerOptionDto> optionDtoList) {
        List<AnswerOption> options = null;
        if (optionDtoList != null) {
            options = optionDtoList.stream().map(this.optionMapper::map).toList();
        }

        return options;
    }

    @Autowired
    public void setOptionMapper(IAnswerOptionMapper optionMapper) {
        this.optionMapper = optionMapper;
    }

    @Autowired
    public void setQuestionMapper(IQuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }
}
