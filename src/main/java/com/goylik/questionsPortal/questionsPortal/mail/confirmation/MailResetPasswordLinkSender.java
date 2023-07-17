package com.goylik.questionsPortal.questionsPortal.mail.confirmation;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailResetPasswordLinkSender extends AbstractMailLinkSender {
    @Autowired
    public MailResetPasswordLinkSender(IUserService userService, JavaMailSender mailSender) {
        super(userService, mailSender);
    }

    private static final String SUBJECT = "Reset password";
    private static final String MESSAGE = "To reset your password follow this link";

    public void sendResetPasswordLink(UserDto user) {
        VerificationTokenDto tokenDto = userService.getVerificationToken(user);
        String token = tokenDto.getToken();
        String url = "/reset-password/" + token;
        sendLink(url, user.getEmail(), SUBJECT, MESSAGE);
    }
}
