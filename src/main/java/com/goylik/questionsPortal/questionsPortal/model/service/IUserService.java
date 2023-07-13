package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    Page<UserDto> findAll(Pageable pageable);
    UserDto findById(Integer id);
    UserDto save(UserDto user);
    void update(UserDto user);
    void delete(UserDto user);
    Page<UserDto> findByLastName(String lastName, Pageable pageable);
    UserDto findByEmail(String email);
    boolean isEmailExist(String email);

    UserDto getUserByToken(String token);
    void createVerificationToken(UserDto user, String token);
    VerificationTokenDto getVerificationToken(String verificationToken);
    VerificationTokenDto getVerificationToken(UserDto user);
}
