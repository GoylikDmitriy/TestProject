package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.service.UserService;
import com.goylik.questionsPortal.questionsPortal.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String login()  {
        return "auth/login";
    }

    @GetMapping("/sign-up")
    public String signup(@ModelAttribute("user") User user) {
        return "auth/signup";
    }

    @PostMapping("/sign-up")
    public String signup(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        this.userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        this.userService.save(user);
        return "redirect:/login";
    }
}
