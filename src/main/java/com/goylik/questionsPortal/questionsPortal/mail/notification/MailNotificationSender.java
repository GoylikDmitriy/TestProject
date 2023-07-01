package com.goylik.questionsPortal.questionsPortal.mail.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailNotificationSender {
    private JavaMailSender mailSender;

    @Autowired
    public MailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Questions Portal Notification");
        mailMessage.setText(message);
        mailMessage.setTo(email);
        this.mailSender.send(mailMessage);
    }
}
