package com.goylik.questionsPortal.questionsPortal.util;

import com.goylik.questionsPortal.questionsPortal.model.AnswerType;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class QuestionValidator implements Validator {
    private final IUserService userService;

    @Autowired
    public QuestionValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return QuestionDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        QuestionDto question = (QuestionDto) target;

        String email = question.getToUser().getEmail();
        UserDto userTo = this.userService.findByEmail(email);
        if (userTo == null) {
            errors.rejectValue("toUser", "","User does not exist.");
        }

        if (email.isEmpty()) {
            errors.rejectValue("toUser", "", "Enter receiver's email.");
        }

        if (email.equals(question.getFromUser().getEmail())) {
            errors.rejectValue("toUser", "", "You cant ask yourself.");
        }

        if (question.getQuestion().isEmpty()) {
            errors.rejectValue("question","","Question cannot be empty.");
        }

        AnswerType answerType = question.getAnswerType();
        if (answerType == AnswerType.RADIO_BUTTON
                || answerType == AnswerType.COMBOBOX
                || answerType == AnswerType.CHECKBOX) {
            if (question.getOptions() == null) {
                errors.rejectValue("options","","Give some options.");
            }
        }
    }
}
