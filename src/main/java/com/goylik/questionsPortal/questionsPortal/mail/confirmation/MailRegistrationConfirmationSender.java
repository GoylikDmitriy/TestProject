package com.goylik.questionsPortal.questionsPortal.mail.confirmation;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MailRegistrationConfirmationSender extends AbstractMailLinkSender {
    @Autowired
    public MailRegistrationConfirmationSender(IUserService userService, JavaMailSender mailSender) {
        super(userService, mailSender);
    }

    private static final String SUBJECT = "Registration Confirmation";
    private static final String MESSAGE = "Confirm registration";

    public void sendConfirmationToken(UserDto user) {
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String url = "/registrationConfirm/" + token;
        sendLink(url, user.getEmail(), SUBJECT, MESSAGE);
    }
}