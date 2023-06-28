package com.goylik.questionsPortal.questionsPortal.model.mapper.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper implements IAnswerMapper {
    private IQuestionMapper questionMapper;

    @Override
    public AnswerDto map(Answer entity) {
        AnswerDto answerDto = null;
        if (entity != null) {
            answerDto = new AnswerDto();
            answerDto.setId(entity.getId());
            answerDto.setAnswer(entity.getAnswer());
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
            answer.setQuestion(this.questionMapper.map(dto.getQuestion()));
        }

        return answer;
    }

    @Autowired
    public void setQuestionMapper(IQuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }
}
