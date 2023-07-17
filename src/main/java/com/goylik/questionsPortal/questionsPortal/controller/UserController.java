package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.mail.notification.MailNotificationSender;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IUserMapper;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.util.EditUserValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final EditUserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final MailNotificationSender notificationSender;
    private final IUserMapper userMapper;

    @Autowired
    public UserController(IUserService userService, EditUserValidator userValidator,
                          PasswordEncoder passwordEncoder, MailNotificationSender notificationSender,
                          IUserMapper userMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.notificationSender = notificationSender;
        this.userMapper = userMapper;
    }

    @GetMapping
    public UserDto getUser() {
        String authEmail = this.getEmailOfAuthenticatedUser();
        UserDto user = this.userService.findByEmail(authEmail);
        user = this.userMapper.mapToEdit(user);
        return user;
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody @Valid UserDto userToUpdate, BindingResult bindingResult) {
        String authEmail = this.getEmailOfAuthenticatedUser();
        this.userValidator.validate(userToUpdate, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        UserDto userDto = this.userService.findByEmail(authEmail);
        String newPassword = userToUpdate.getConfirmedPassword();
        String password = newPassword.isEmpty() ? userDto.getPassword() : this.passwordEncoder.encode(newPassword);

        userToUpdate.setPassword(password);
        userToUpdate.setEnabled(userDto.isEnabled());
        userToUpdate.setIncomingQuestions(userDto.getIncomingQuestions());
        userToUpdate.setOutgoingQuestions(userDto.getOutgoingQuestions());
        this.userService.update(userToUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody UserDto userDto) {
        String password = userDto.getPassword();
        String authEmail = this.getEmailOfAuthenticatedUser();
        UserDto user = this.userService.findByEmail(authEmail);
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong password");
        }

        this.userService.delete(user);
        this.notificationSender.send(authEmail, "Your account has been deleted");
        return ResponseEntity.ok().body("Your account has been deleted");
    }

    private String getEmailOfAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }

        return null;
    }
}