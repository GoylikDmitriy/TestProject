package com.goylik.questionsPortal.questionsPortal.confirmation;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MailConfirmationSender {
    private IUserService userService;
    private JavaMailSender mailSender;

    @Autowired
    public MailConfirmationSender(IUserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Value("${server.address}")
    private String host;
    @Value("${server.port}")
    private String port;

    public void sendConformationToken(UserDto user, ConfirmationType confirmationType, String subject, String message) {
        String token = UUID.randomUUID().toString();
        this.userService.createVerificationToken(user, token);
        String confirmationUrl = confirmationType.getAddress() + "?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://" + this.host + ":" + this.port + confirmationUrl);
        this.mailSender.send(email);
    }
}
