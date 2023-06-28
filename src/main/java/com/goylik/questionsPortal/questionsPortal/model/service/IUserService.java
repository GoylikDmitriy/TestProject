package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;

import java.util.List;

public interface IUserService {
    List<UserDto> findAll();
    UserDto findById(Integer id);
    UserDto save(UserDto user);
    void update(UserDto user);
    void delete(UserDto user);
    List<UserDto> findByLastName(String lastName);
    UserDto findByEmail(String email);
    boolean isEmailExist(String email);

    void createVerificationToken(UserDto user, String token);
    VerificationTokenDto getVerificationToken(String verificationToken);

}
