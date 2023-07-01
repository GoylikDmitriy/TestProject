package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.mail.notification.MailNotificationSender;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.util.EditUserValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final EditUserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final MailNotificationSender notificationSender;

    @Autowired
    public UserController(IUserService userService, EditUserValidator userValidator,
                          PasswordEncoder passwordEncoder, MailNotificationSender notificationSender) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.notificationSender = notificationSender;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", this.userService.findById(id));
        return "user/user";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        String authEmail = this.getEmailOfAuthenticatedUser();
        UserDto user = this.userService.findById(id);
        if (!user.getEmail().equals(authEmail)) {
            return "redirect:/user/{id}";
        }

        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id,
                       @ModelAttribute("user") @Valid UserDto userToUpdate, BindingResult bindingResult) {
        this.userValidator.validate(userToUpdate, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        UserDto userDto = this.userService.findById(id);
        String newPassword = userToUpdate.getConfirmedPassword();
        String password = newPassword.isEmpty() ? userDto.getPassword() : this.passwordEncoder.encode(newPassword);

        userToUpdate.setPassword(password);
        userToUpdate.setEnabled(userDto.isEnabled());
        userToUpdate.setIncomingQuestions(userDto.getIncomingQuestions());
        userToUpdate.setOutgoingQuestions(userDto.getOutgoingQuestions());
        this.userService.update(userToUpdate);
        return "redirect:/user/{id}";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model) {
        UserDto user = this.userService.findById(id);
        if (user.getEmail().equals(this.getEmailOfAuthenticatedUser())) {
            model.addAttribute("id", id);
            return "user/delete";
        }

        return "redirect:/user/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model, HttpSession session,
                         @RequestParam("password") String password) {
        UserDto user = this.userService.findById(id);
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("password", "wrong password");
            return "user/delete";
        }

        String email = user.getEmail();
        this.userService.delete(user);
        session.invalidate();
        this.notificationSender.send(email, "Your account has been deleted.");
        return "redirect:/sign-up";
    }

    private String getEmailOfAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }

        return null;
    }
}