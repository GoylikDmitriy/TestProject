package com.goylik.questionsPortal.questionsPortal.util;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public boolean isPasswordStrong(String password) {
        return password.matches(".*[!@#$%^&*].*")
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*"
        );
    }

    public boolean isPasswordMatch(String password, String matchPassword) {
        return password.equals(matchPassword);
    }

    public boolean isPasswordNew(String password, String newPassword) {
        return !this.isPasswordMatch(password, newPassword);
    }
}
