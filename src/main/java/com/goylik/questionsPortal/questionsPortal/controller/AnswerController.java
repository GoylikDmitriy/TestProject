package com.goylik.questionsPortal.questionsPortal.controller;

import com.goylik.questionsPortal.questionsPortal.model.dto.AnswerDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IAnswerMapper;
import com.goylik.questionsPortal.questionsPortal.model.service.IAnswerService;
import com.goylik.questionsPortal.questionsPortal.model.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    private final IAnswerService answerService;
    private final IQuestionService questionService;
    private final IAnswerMapper answerMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AnswerController(IAnswerService answerService, IQuestionService questionService,
                            IAnswerMapper answerMapper, SimpMessagingTemplate messagingTemplate) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.answerMapper = answerMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteAnswer(@RequestBody AnswerDto answerDto) {
        AnswerDto answerToDelete = this.answerService.findById(answerDto.getId());
        this.answerService.delete(answerToDelete);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editAnswer(@RequestBody AnswerDto answerDto) {
        if (answerDto.getAnswer().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Answer cannot be empty.");
        }

        this.answerService.update(answerDto);
        return ResponseEntity.ok().body(answerDto);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAnswer(@RequestBody AnswerDto answerDto) {
        if (answerDto.getAnswer().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Answer cannot be empty.");
        }

        QuestionDto questionDto = this.questionService.findById(answerDto.getQuestion().getId());
        answerDto.setQuestion(questionDto);
        answerDto = this.answerService.save(answerDto);
        return ResponseEntity.ok().body(answerDto);
    }

    @MessageMapping("/answer")
    public void notifyAnswer(@Payload Integer answerId) {
        AnswerDto answerDto = this.answerService.findById(answerId);
        String email = answerDto.getQuestion().getFromUser().getEmail();
        AnswerDto answerToSend = this.answerMapper.mapToShow(answerDto);
        this.messagingTemplate.convertAndSendToUser(email, "/answer", answerToSend);
    }

    @MessageMapping("/answer-delete")
    public void notifyDeleteAnswer(@Payload Integer answerId) {
        AnswerDto answerDto = this.answerService.findById(answerId);
        String email = answerDto.getQuestion().getFromUser().getEmail();
        AnswerDto answerToDelete = this.answerMapper.mapToShow(answerDto);
        this.messagingTemplate.convertAndSendToUser(email, "/answer-delete", answerToDelete);
    }
}