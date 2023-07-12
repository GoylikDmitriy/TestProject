package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    private User user() {
        User user = new User("Denny", "Cooper", "denny.cooper@gmail.com",
                "$2a$10$2f6TA44Cj/BRSdvLG5sZtu3PLHr3hix2bjWjoIHK00IAqT3RgSrj.", "880231243");
        user.setId(1);
        return user;
    }

    @BeforeEach
    void setup() {
        given(this.userRepository.findById(1)).willReturn(Optional.of(user()));
    }

    @Test
    @WithMockUser
    public void editUser() throws Exception {
        mockMvc
                .perform(post("/user/1/edit").with(csrf())
                        .param("firstName", "Denny")
                        .param("lastName", "Last")
                        .param("email", "denny.cooper@gmail.com")
                        .param("password", "01234567")
                        .param("confirmedPassword", "")
                        .param("phoneNumber", "421414352")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void editUserWrongPassword() throws Exception {
        mockMvc
                .perform(post("/user/1/edit").with(csrf())
                        .param("firstName", "Denny")
                        .param("lastName", "Last")
                        .param("email", "denny.cooper@gmail.com")
                        .param("password", "@rte2")
                        .param("confirmedPassword", "")
                        .param("phoneNumber", "421414352")
                )
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(view().name("user/edit"));
    }

    @Test
    @WithMockUser
    public void editUserPasswordNotStrong() throws Exception {
        mockMvc
                .perform(post("/user/1/edit").with(csrf())
                        .param("firstName", "Denny")
                        .param("lastName", "Last")
                        .param("email", "denny.cooper@gmail.com")
                        .param("password", "01234567")
                        .param("confirmedPassword", "qwerty")
                        .param("phoneNumber", "421414352")
                )
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "confirmedPassword"))
                .andExpect(view().name("user/edit"));
    }

    @Test
    @WithMockUser
    public void deleteUserWrongPassword() throws Exception {
        mockMvc
                .perform(post("/user/1/delete").with(csrf())
                        .param("id", "1")
                        .param("password", "qwerty")
                )
                .andExpect(model().attributeExists("password"));
    }

    @Test
    @WithMockUser
    public void deleteUser() throws Exception {
        mockMvc
                .perform(post("/user/1/delete").with(csrf())
                        .param("id", "1")
                        .param("password", "01234567")
                )
                .andExpect(status().is3xxRedirection());
    }
}
