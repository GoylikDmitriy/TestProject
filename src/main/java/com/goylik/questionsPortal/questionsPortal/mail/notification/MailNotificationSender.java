package com.goylik.questionsPortal.questionsPortal.mail.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailNotificationSender {
    private final JavaMailSender mailSender;

    @Autowired
    public MailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    private static final String SUBJECT = "Questions Portal Notification";

    public void send(String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(message);
        mailMessage.setTo(email);
        this.mailSender.send(mailMessage);
    }
}
