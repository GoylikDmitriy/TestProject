package com.goylik.questionsPortal.questionsPortal.util;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Calendar;

@Component
public class VerificationTokenValidator {
    public boolean isTokenNull(VerificationTokenDto token, Model model) {
        if (token == null) {
            model.addAttribute("message", "invalid token");
        }

        return token == null;
    }

    public boolean isTokenExpired(VerificationTokenDto token, Model model) {
        Calendar cal = Calendar.getInstance();
        if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "token is expired");
            model.addAttribute("expired", true);
            model.addAttribute("token", token.getToken());
            return true;
        }

        return false;
    }
}
