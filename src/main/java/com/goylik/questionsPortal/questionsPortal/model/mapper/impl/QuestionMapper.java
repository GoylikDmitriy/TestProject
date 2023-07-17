package com.goylik.questionsPortal.questionsPortal.model.mapper.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerOptionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Answer;
import com.goylik.questionsPortal.questionsPortal.model.entity.AnswerOption;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerOptionMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper implements IQuestionMapper {
    private IAnswerOptionMapper optionMapper;

    @Override
    public QuestionDto mapToShow(Question entity) {
        QuestionDto questionDto = null;
        if (entity != null) {
            questionDto = new QuestionDto();
            questionDto.setId(entity.getId());
            questionDto.setQuestion(entity.getQuestion());
            questionDto.setAnswerType(entity.getAnswerType());
            questionDto.setOptions(this.convertOptions(entity.getOptions()));
            questionDto.setAnswer(convertAnswerToShowQuestion(entity.getAnswer()));
            questionDto.setFromUser(convertUser(entity.getFromUser()));
            questionDto.setToUser(convertUser(entity.getToUser()));
        }

        return questionDto;
    }

    @Override
    public QuestionDto map(Question entity) {
        QuestionDto questionDto = null;
        if (entity != null) {
            questionDto = new QuestionDto();
            questionDto.setId(entity.getId());
            questionDto.setQuestion(entity.getQuestion());
            questionDto.setAnswerType(entity.getAnswerType());
            questionDto.setOptions(this.convertOptions(entity.getOptions()));
            questionDto.setAnswer(this.convertAnswer(entity.getAnswer(), questionDto));
            questionDto.setToUser(this.convertUser(entity.getToUser()));
            questionDto.setFromUser(this.convertUser(entity.getFromUser()));
        }

        return questionDto;
    }

    @Override
    public Question map(QuestionDto dto) {
        Question question = null;
        if (dto != null) {
            question = new Question();
            question.setId(dto.getId());
            question.setQuestion(dto.getQuestion());
            question.setAnswerType(dto.getAnswerType());
            question.setOptions(this.convertOptionDtoList(dto.getOptions()));
            question.setAnswer(this.convertAnswerDto(dto.getAnswer(), question));
            question.setToUser(this.convertUserDto(dto.getToUser()));
            question.setFromUser(this.convertUserDto(dto.getFromUser()));
        }

        return question;
    }

    private UserDto convertUser(User user) {
        UserDto userDto = null;
        if (user != null) {
            userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
        }

        return userDto;
    }

    private User convertUserDto(UserDto userDto) {
        User user = null;
        if (userDto != null) {
            user = new User();
            user.setId(userDto.getId());
            user.setEmail(userDto.getEmail());
        }

        return user;
    }

    private AnswerDto convertAnswer(Answer answer, QuestionDto questionDto) {
        AnswerDto answerDto = null;
        if (answer != null) {
            answerDto = new AnswerDto();
            answerDto.setId(answer.getId());
            answerDto.setAnswer(answer.getAnswer());
            answerDto.setOptions(this.convertOptions(answer.getOptions()));
            answerDto.setQuestion(questionDto);
        }

        return answerDto;
    }

    private Answer convertAnswerDto(AnswerDto answerDto, Question question) {
        Answer answer = null;
        if (answerDto != null) {
            answer = new Answer();
            answer.setId(answerDto.getId());
            answer.setAnswer(answerDto.getAnswer());
            answer.setOptions(this.convertOptionDtoList(answerDto.getOptions()));
            answer.setQuestion(question);
        }

        return answer;
    }

    private AnswerDto convertAnswerToShowQuestion(Answer answer) {
        AnswerDto answerDto = null;
        if (answer != null) {
            answerDto = new AnswerDto();
            answerDto.setId(answer.getId());
            answerDto.setAnswer(answer.getAnswer());
            answerDto.setOptions(this.convertOptions(answer.getOptions()));
        }

        return answerDto;
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
}
