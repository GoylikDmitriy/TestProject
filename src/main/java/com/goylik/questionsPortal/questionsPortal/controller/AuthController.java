package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.util.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final IUserService userService;
    private final UserValidator userValidator;
    private final ModelMapper mapper;

    @Autowired
    public AuthController(IUserService userService, UserValidator userValidator, ModelMapper mapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.mapper = mapper;
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

        User user = mapper.map(userDto, User.class);
        this.userService.save(user);
        return "redirect:/login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }
}