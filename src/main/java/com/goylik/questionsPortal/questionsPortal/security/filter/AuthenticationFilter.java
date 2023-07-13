package com.goylik.questionsPortal.questionsPortal.security.filter;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private IUserService userService;
    private final List<String> unauthURLs = Arrays.asList(
            "/login", "/sign-up","/error", "/reset-password", "/forgot-password",
            "/registrationConfirm", "/resendVerificationToken", "/ws"
    );

    @Lazy
    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String uri = String.format("/%s", requestUri.split("/")[1]);
        String token = request.getHeader("Authorization");
        if (!this.unauthURLs.contains(uri)) {
            if (token == null) {
                response.setStatus(401);
                return;
            } else {
                VerificationTokenDto tokenDto = userService.getVerificationToken(token);
                UserDto userDto = userService.getUserByToken(token);
                if (tokenDto != null) {
                    UserDetails userDetails = new UserDetailsImpl(userDto);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
