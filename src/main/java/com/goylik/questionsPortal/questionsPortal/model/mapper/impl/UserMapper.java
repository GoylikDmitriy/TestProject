package com.goylik.questionsPortal.questionsPortal.model.mapper.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IQuestionMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper implements IUserMapper {

    private IQuestionMapper questionMapper;

    @Override
    public UserDto mapToEdit(UserDto dto) {
        UserDto userDto = null;
        if (dto != null) {
            userDto = new UserDto();
            userDto.setId(dto.getId());
            userDto.setEmail(dto.getEmail());
            userDto.setFirstName(dto.getFirstName());
            userDto.setLastName(dto.getLastName());
            userDto.setPhoneNumber(dto.getPhoneNumber());
            userDto.setPassword("");
            userDto.setConfirmedPassword("");
        }

        return userDto;
    }

    @Override
    public UserDto map(User entity) {
        UserDto userDto = null;
        if (entity != null) {
            userDto = new UserDto();
            userDto.setId(entity.getId());
            userDto.setEmail(entity.getEmail());
            userDto.setPassword(entity.getPassword());
            userDto.setFirstName(entity.getFirstName());
            userDto.setLastName(entity.getLastName());
            userDto.setPhoneNumber(entity.getPhoneNumber());
            userDto.setEnabled(entity.isEnabled());
            userDto.setIncomingQuestions(this.convertQuestions(entity.getIncomingQuestions()));
            userDto.setOutgoingQuestions(this.convertQuestions(entity.getOutgoingQuestions()));
        }

        return userDto;
    }

    @Override
    public User map(UserDto dto) {
        User user = null;
        if (dto != null) {
            user = getUser(dto);
            user.setEnabled(dto.isEnabled());
            user.setIncomingQuestions(this.convertQuestionDtoList(dto.getIncomingQuestions()));
            user.setOutgoingQuestions(this.convertQuestionDtoList(dto.getOutgoingQuestions()));
        }

        return user;
    }

    @Override
    public User mapToRegistration(UserDto dto) {
        User user = null;
        if (dto != null) {
            user = getUser(dto);
        }

        return user;
    }

    private User getUser(UserDto dto) {
        User user;
        user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }

    private List<QuestionDto> convertQuestions(List<Question> questions) {
        List<QuestionDto> questionDtoList = null;
        if (questions != null) {
            questionDtoList = questions.stream().map(this.questionMapper::map).toList();
        }

        return questionDtoList;
    }

    private List<Question> convertQuestionDtoList(List<QuestionDto> questionDtoList) {
        List<Question> questions = null;
        if (questionDtoList != null) {
            questions = questionDtoList.stream().map(this.questionMapper::map).toList();
        }

        return questions;
    }

    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }
}
