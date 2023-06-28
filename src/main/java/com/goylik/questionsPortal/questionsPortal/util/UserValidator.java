package com.goylik.questionsPortal.questionsPortal.util;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final IUserService userService;

    @Autowired
    public UserValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if (this.userService.isEmailExist(userDto.getEmail())) {
            errors.rejectValue("email", "", "user with this email is already exist");
        }

        String password = userDto.getPassword();
        if (!password.matches(".*[!@#$%^&*].*")
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[0-9].*")) {
            errors.rejectValue("password", "",
                    "password must contain at least 1 uppercase letter 1 specific character and 1 digit number");
        }

        if (!password.equals(userDto.getConfirmedPassword())) {
            errors.rejectValue("confirmedPassword", "", "passwords do not match");
        }
    }
}
