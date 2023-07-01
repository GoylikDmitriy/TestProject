package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.repository.AnswerRepository;
import com.goylik.questionsPortal.questionsPortal.model.repository.QuestionRepository;
import com.goylik.questionsPortal.questionsPortal.model.repository.UserRepository;
import com.goylik.questionsPortal.questionsPortal.model.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JavaMailSender mailSender;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AnswerRepository answerRepository;
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private VerificationTokenRepository tokenRepository;

    private User user() {
        User user = new User("Denny", "Cooper", "denny.cooper@gmail.com",
                "01234567", "880231243");
        user.setId(1);
        return user;
    }

    private User successfulUser() {
        User user = new User("Name", "Last", "test@mail.com",
                "t3St$", "421414352");
        return user;
    }

    private User successfulUserWithId() {
        User user = new User("Name", "Last", "test@mail.com",
                "t3St$", "421414352");
        user.setId(5);
        return user;
    }

    @BeforeEach
    void setup() {
        given(this.userRepository.findByEmail("denny.cooper@gmail.com")).willReturn(user());
        given(this.userRepository.save(successfulUser())).willReturn(successfulUserWithId());
    }

    @Test
    public void testSignupSuccessful() throws Exception {
        mockMvc
                .perform(post("/sign-up").with(csrf())
                        .param("firstName", "Name")
                        .param("lastName", "Last")
                        .param("email", "test@mail.com")
                        .param("password", "t3St$")
                        .param("confirmedPassword", "t3St$")
                        .param("phoneNumber", "421414352")
                ).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testSignupUserAlreadyExistHasErrors() throws Exception {
        mockMvc
                .perform(post("/sign-up").with(csrf())
                        .param("firstName", "Denny")
                        .param("lastName", "Last")
                        .param("email", "denny.cooper@gmail.com")
                        .param("password", "t3St$")
                        .param("confirmedPassword", "t3St$")
                        .param("phoneNumber", "421414352")
                )
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(view().name("auth/signup"));
    }

    @Test
    public void testSignupIncorrectPasswordHasError() throws Exception {
        mockMvc
                .perform(post("/sign-up").with(csrf())
                        .param("firstName", "Name")
                        .param("lastName", "Last")
                        .param("email", "test@mail.com")
                        .param("password", "012345")
                        .param("confirmedPassword", "012345")
                        .param("phoneNumber", "421414352")
                )
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(view().name("auth/signup"));
    }

    @Test
    public void testSignupPasswordsDoNotMatch() throws Exception {
        mockMvc
                .perform(post("/sign-up").with(csrf())
                        .param("firstName", "Name")
                        .param("lastName", "Last")
                        .param("email", "test@mail.com")
                        .param("password", "t3St$")
                        .param("confirmedPassword", "t3StS")
                        .param("phoneNumber", "421414352")
                )
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "confirmedPassword"))
                .andExpect(view().name("auth/signup"));
    }
}