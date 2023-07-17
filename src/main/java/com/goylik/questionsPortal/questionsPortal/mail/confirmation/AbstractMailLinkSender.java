package com.goylik.questionsPortal.questionsPortal.mail.confirmation;

import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class AbstractMailLinkSender {

    @Value("${server.address}")
    protected String host;
    @Value("${server.client-port}")
    protected String port;

    protected IUserService userService;
    protected JavaMailSender mailSender;

    public AbstractMailLinkSender(IUserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    protected void sendLink(String url, String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(String.format("%s%nhttp://%s:%s%s", message, this.host, this.port, url));
        this.mailSender.send(mailMessage);
    }
}
