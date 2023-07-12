package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.mail.confirmation.MailResetPasswordLinkSender;
import com.goylik.questionsPortal.questionsPortal.mail.notification.MailNotificationSender;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.util.PasswordValidator;
import com.goylik.questionsPortal.questionsPortal.util.UserValidator;
import com.goylik.questionsPortal.questionsPortal.mail.confirmation.MailRegistrationConfirmationSender;
import com.goylik.questionsPortal.questionsPortal.util.VerificationTokenValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final IUserService userService;
    private final UserValidator userValidator;
    private final VerificationTokenValidator tokenValidator;
    private final MailRegistrationConfirmationSender mailConfirmationSender;
    private final MailResetPasswordLinkSender mailResetPasswordLinkSender;
    private final MailNotificationSender notificationSender;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(IUserService userService, UserValidator userValidator,
                          VerificationTokenValidator tokenValidator,
                          MailRegistrationConfirmationSender mailConfirmationSender,
                          MailResetPasswordLinkSender mailResetPasswordLinkSender,
                          MailNotificationSender notificationSender, PasswordValidator passwordValidator,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.tokenValidator = tokenValidator;
        this.mailConfirmationSender = mailConfirmationSender;
        this.mailResetPasswordLinkSender = mailResetPasswordLinkSender;
        this.notificationSender = notificationSender;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (!this.userService.isEmailExist(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account with this email does not exist");
        }

        UserDto user = this.userService.findByEmail(email);
        this.mailResetPasswordLinkSender.sendResetPasswordLink(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDto userToUpdate) {
        String email = userToUpdate.getEmail();
        String password = userToUpdate.getPassword();
        String confirmedPassword = userToUpdate.getConfirmedPassword();
        if (!this.passwordValidator.isPasswordStrong(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("password must contain at least 1 uppercase letter 1 specific character and 1 digit number");
        }

        if (!this.passwordValidator.isPasswordMatch(password, confirmedPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("passwords do not match");
        }

        UserDto user = this.userService.findByEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        this.userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        this.userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        userDto = this.userService.save(userDto);
        this.mailConfirmationSender.sendConfirmationToken(userDto);
        return ResponseEntity.ok(this.userService.getVerificationToken(userDto).getToken());
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        VerificationTokenDto verificationTokenDto = this.userService.getVerificationToken(token);
        if (verificationTokenDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not valid");
        }

        if (this.tokenValidator.isExpired(verificationTokenDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is expired");
        }

        UserDto user = verificationTokenDto.getUser();
        user.setEnabled(true);
        this.userService.update(user);
        this.notificationSender.send(user.getEmail(), "Your account has been created.");
        return ResponseEntity.ok(user.getEmail());
    }

    @GetMapping("/resendVerificationToken")
    public ResponseEntity<String> resendVerificationToken(@RequestParam("token") String token) {
        VerificationTokenDto verificationToken = this.userService.getVerificationToken(token);
        if (verificationToken != null) {
            UserDto user = verificationToken.getUser();
            this.mailConfirmationSender.sendConfirmationToken(user);
            return ResponseEntity.ok(this.userService.getVerificationToken(user).getToken());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not valid");
    }

    @GetMapping("/isConfirmed")
    public Boolean isConfirmed(@RequestParam String token) {
        VerificationTokenDto tokenDto = this.userService.getVerificationToken(token);
        return tokenDto != null && tokenDto.getUser().isEnabled();
    }
}
