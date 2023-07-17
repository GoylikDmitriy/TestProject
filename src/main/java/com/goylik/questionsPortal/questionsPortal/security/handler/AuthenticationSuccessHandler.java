package com.goylik.questionsPortal.questionsPortal.security.handler;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationSuccessHandler
        implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private IUserService userService;

    @Lazy
    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDto user = this.userService.findByEmail(authentication.getName());
        String token = this.userService.getVerificationToken(user).getToken();
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("{\"username\":\"" + authentication.getName() + "\"," +
                "\"token\":\"" + token + "\"}");
        writer.close();
    }
}
