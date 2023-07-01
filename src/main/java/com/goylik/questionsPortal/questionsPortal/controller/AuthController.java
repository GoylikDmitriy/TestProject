package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.mail.notification.MailNotificationSender;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.util.UserValidator;
import com.goylik.questionsPortal.questionsPortal.mail.confirmation.MailRegistrationConfirmationSender;
import com.goylik.questionsPortal.questionsPortal.util.VerificationTokenValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class AuthController {
    private final IUserService userService;
    private final UserValidator userValidator;
    private final VerificationTokenValidator tokenValidator;
    private final MailRegistrationConfirmationSender mailConfirmationSender;
    private final MailNotificationSender notificationSender;

    @Autowired
    public AuthController(IUserService userService, UserValidator userValidator,
                          VerificationTokenValidator tokenValidator,
                          MailRegistrationConfirmationSender mailConfirmationSender,
                          MailNotificationSender notificationSender) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.tokenValidator = tokenValidator;
        this.mailConfirmationSender = mailConfirmationSender;
        this.notificationSender = notificationSender;
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
    public String signup(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult) {
        this.userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        userDto = this.userService.save(userDto);
        this.mailConfirmationSender.sendConfirmationToken(userDto);
        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(Model model, @RequestParam("token") String token) {
        VerificationTokenDto verificationToken = this.userService.getVerificationToken(token);
        if (this.tokenValidator.isTokenNull(verificationToken, model)) {
            return "auth/tokenError";
        }

        if (this.tokenValidator.isTokenExpired(verificationToken, model)) {
            return "auth/tokenError";
        }

        UserDto user = verificationToken.getUser();
        user.setEnabled(true);
        this.userService.update(user);
        this.notificationSender.send(user.getEmail(), "Your account has been created.");
        return "redirect:/login";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String token) {
        VerificationTokenDto verificationToken = this.userService.getVerificationToken(token);
        UserDto user = verificationToken.getUser();
        this.mailConfirmationSender.sendConfirmationToken(user);
        return "redirect:/login";
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }
}