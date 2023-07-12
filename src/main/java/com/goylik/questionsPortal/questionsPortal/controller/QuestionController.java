package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.AnswerType;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IQuestionMapper;
import com.goylik.questionsPortal.questionsPortal.model.service.IQuestionService;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import com.goylik.questionsPortal.questionsPortal.util.QuestionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final Integer PAGE_SIZE = 5;
    private final IQuestionService questionService;
    private final IUserService userService;
    private final QuestionValidator questionValidator;
    private final IQuestionMapper questionMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public QuestionController(IQuestionService questionService, IUserService userService,
                              QuestionValidator questionValidator, IQuestionMapper questionMapper,
                              SimpMessagingTemplate messagingTemplate) {
        this.questionService = questionService;
        this.userService = userService;
        this.questionValidator = questionValidator;
        this.questionMapper = questionMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/answer-types")
    public List<String> getAnswerTypes() {
        return Arrays.stream(AnswerType.values())
                .map(Enum::name)
                .toList();
    }

    @GetMapping("/get/{id}")
    public QuestionDto getQuestion(@PathVariable Integer id) {
        QuestionDto question = this.questionService.findById(id);
        return this.questionMapper.mapToShow(this.questionMapper.map(question));
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editQuestion(@RequestBody QuestionDto questionToUpdate, BindingResult bindingResult) {
        QuestionDto question = this.questionService.findById(questionToUpdate.getId());
        UserDto authUser = this.getAuthenticatedUser();
        if (!question.getFromUser().equals(authUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        questionToUpdate.setFromUser(authUser);
        this.questionValidator.validate(questionToUpdate, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        question.setQuestion(questionToUpdate.getQuestion());
        question.setAnswerType(questionToUpdate.getAnswerType());
        question.setOptions(questionToUpdate.getOptions());
        this.questionService.update(question);
        return ResponseEntity.ok().body(question);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDto questionToAdd, BindingResult bindingResult) {
        UserDto authUser = this.getAuthenticatedUser();
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        questionToAdd.setFromUser(authUser);
        this.questionValidator.validate(questionToAdd, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        UserDto userTo = this.userService.findByEmail(questionToAdd.getToUser().getEmail());
        questionToAdd.setToUser(userTo);
        questionToAdd = this.questionService.save(questionToAdd);
        return ResponseEntity.ok().body(questionToAdd);
    }

    @MessageMapping("/question-add")
    public void notifyAddQuestion(@Payload Integer questionId) {
        this.notifyQuestion(questionId, "/question-add");
    }

    @MessageMapping("/question-edit")
    public void notifyEditQuestion(@Payload Integer questionId) {
        this.notifyQuestion(questionId, "/question-edit");
    }

    @MessageMapping("/question-delete")
    public void notifyDeleteQuestion(@Payload Integer questionId) {
        this.notifyQuestion(questionId, "/question-delete");
    }

    private void notifyQuestion(Integer questionId, String destination) {
        QuestionDto questionDto = this.questionService.findById(questionId);
        questionDto = this.questionMapper.mapToShow(this.questionMapper.map(questionDto));
        this.messagingTemplate.convertAndSendToUser(questionDto.getToUser().getEmail(), destination, questionDto);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteQuestion(@RequestBody QuestionDto questionDto) {
        QuestionDto questionToDelete = this.questionService.findById(questionDto.getId());
        this.questionService.delete(questionToDelete);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/incoming/{page}")
    public ResponseEntity<?> getIncomingQuestions(@PathVariable("page") Integer page) {
        UserDto user = this.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<QuestionDto> questions = this.questionService.getIncomingQuestionsByUserId(user.getId(), pageable);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/outgoing/{page}")
    public ResponseEntity<?> getOutgoingQuestions(@PathVariable("page") Integer page) {
        UserDto user = this.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<QuestionDto> questions = this.questionService.getOutgoingQuestionsByUserId(user.getId(), pageable);
        return ResponseEntity.ok(questions);
    }

    private UserDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            return this.userService.findByEmail(email);
        }

        return null;
    }
}
