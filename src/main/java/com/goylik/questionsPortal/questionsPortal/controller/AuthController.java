package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.entity.token.VerificationToken;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.event.OnRegistrationCompleteEvent;
import com.goylik.questionsPortal.questionsPortal.util.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;

@Controller
public class AuthController {
    private final IUserService userService;
    private final UserValidator userValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuthController(IUserService userService, UserValidator userValidator,
                          ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/login")
    public String login()  {
        if (!isAuthenticated()) {
            return "auth/login";
        }

        return "redirect:/home";
    }

    @GetMapping("/sign-up")
    public String signup(@ModelAttribute("user") UserDto userDto) {
        if (!isAuthenticated()) {
            return "auth/signup";
        }

        return "redirect:/home";
    }

    @PostMapping("/sign-up")
    public String signup(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult,
                         HttpServletRequest request) {
        this.userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        userDto = this.userService.save(userDto);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDto, appUrl));
        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(Model model, @RequestParam("token") String token) {
        VerificationTokenDto verificationToken = this.userService.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("message", "invalid token");
            return "auth/tokenError";
        }

        UserDto user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "token is expired");
            model.addAttribute("expired", true);
            model.addAttribute("token", verificationToken.getToken());
            return "auth/tokenError";
        }

        user.setEnabled(true);
        this.userService.update(user);
        return "redirect:/login";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String token, HttpServletRequest request) {
        VerificationTokenDto verificationToken = this.userService.getVerificationToken(token);
        UserDto user = verificationToken.getUser();
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
        return "redirect:/login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }
}