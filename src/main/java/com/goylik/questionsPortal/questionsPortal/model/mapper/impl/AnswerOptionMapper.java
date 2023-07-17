package com.goylik.questionsPortal.questionsPortal.model.mapper.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerOptionMapper;
import org.springframework.stereotype.Component;

@Component
public class AnswerOptionMapper implements IAnswerOptionMapper {
    @Override
    public AnswerOptionDto map(AnswerOption entity) {
        AnswerOptionDto optionDto = null;
        if (entity != null) {
            optionDto = new AnswerOptionDto();
            optionDto.setId(entity.getId());
            optionDto.setOption(entity.getOption());
        }

        return optionDto;
    }

    @Override
    public AnswerOption map(AnswerOptionDto dto) {
        AnswerOption option = null;
        if (dto != null) {
            option = new AnswerOption();
            option.setId(dto.getId());
            option.setOption(dto.getOption());
        }

        return option;
    }
}
