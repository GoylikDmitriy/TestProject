package com.goylik.questionsPortal.questionsPortal.util;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EditUserValidator implements Validator {
    private final IUserService userService;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EditUserValidator(IUserService userService, PasswordValidator passwordValidator,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto updatedUser = (UserDto) target;
        UserDto oldUser = this.userService.findById(updatedUser.getId());

        String currentPassword = updatedUser.getPassword();
        String newPassword = updatedUser.getConfirmedPassword();
        if (!this.passwordEncoder.matches(currentPassword, oldUser.getPassword())) {
            errors.rejectValue("password", "", "wrong password");
        }

        if (!newPassword.isEmpty()) {
            if (!this.passwordValidator.isPasswordNew(currentPassword, newPassword)) {
                errors.rejectValue("confirmedPassword", "", "the new password is the same as old one");
            }

            if (!this.passwordValidator.isPasswordStrong(newPassword)) {
                errors.rejectValue("confirmedPassword", "",
                        "password must contain at least 1 uppercase letter 1 specific character and 1 digit number");
            }
        }

        String newEmail = updatedUser.getEmail();
        if (!newEmail.equals(oldUser.getEmail())) {
            if (this.userService.isEmailExist(updatedUser.getEmail())) {
                errors.rejectValue("email", "", "user with this email is already exist");
            }
        }
    }
}