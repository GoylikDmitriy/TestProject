package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.AnswerType;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest extends ControllerTest {

    private User user() {
        User user = new User("Denny", "Cooper", "denny.cooper@gmail.com",
                "$2a$10$2f6TA44Cj/BRSdvLG5sZtu3PLHr3hix2bjWjoIHK00IAqT3RgSrj.", "880231243");
        user.setId(1);
        return user;
    }

    private Page<Question> incomingQuestions() {
        Question question = new Question("What is your name?", AnswerType.SINGLE_LINE);
        question.setFromUser(user());
        return new PageImpl<>(List.of(question));
    }

    private Page<Question> outgoingQuestions() {
        Question question1 = new Question("Do you like it?", AnswerType.SINGLE_LINE);
        Question question2 = new Question("Are you ok?", AnswerType.COMBOBOX);
        question1.setToUser(user());
        question2.setToUser(user());
        return new PageImpl<>(List.of(question1, question2));
    }

    @BeforeEach
    void setup() {
        given(this.userRepository.findByEmail("denny.cooper@gmail.com")).willReturn(user());
        given(this.questionRepository.getIncomingQuestionsByUserId(1, Pageable.ofSize( 5))).willReturn(incomingQuestions());
        given(this.questionRepository.getOutgoingQuestionsByUserId(1, Pageable.ofSize(5))).willReturn(outgoingQuestions());
    }

    @Test
    @WithMockUser(username = "denny.cooper@gmail.com")
    public void getIncomingQuestions() throws Exception {
        mockMvc
                .perform(get("/questions/incoming/1").with(csrf()))
                .andExpect(view().name("question/incoming"));
    }

    @Test
    @WithMockUser(username = "denny.cooper@gmail.com")
    public void getOutgoingQuestions() throws Exception {
        mockMvc
                .perform(get("/questions/outgoing/1").with(csrf()))
                .andExpect(view().name("question/outgoing"));
    }
}
