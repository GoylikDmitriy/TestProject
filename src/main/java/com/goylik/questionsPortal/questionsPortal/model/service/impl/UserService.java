package com.goylik.questionsPortal.questionsPortal.model.service.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.entity.token.VerificationToken;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IUserMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IVerificationTokenMapper;
import com.goylik.questionsPortal.questionsPortal.model.repository.UserRepository;
import com.goylik.questionsPortal.questionsPortal.model.repository.VerificationTokenRepository;
import com.goylik.questionsPortal.questionsPortal.model.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final AnswerService answerService;
    private final IUserMapper userMapper;
    private final IVerificationTokenMapper tokenMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository,
                       AnswerService answerService, IUserMapper userMapper,
                       IVerificationTokenMapper tokenMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.answerService = answerService;
        this.userMapper = userMapper;
        this.tokenMapper = tokenMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public VerificationTokenDto getVerificationToken(String verificationToken) {
        VerificationToken token = this.tokenRepository.findByToken(verificationToken);
        VerificationTokenDto tokenDto = this.tokenMapper.map(token);
        return tokenDto;
    }

    @Override
    public void createVerificationToken(UserDto userDto, String token) {
        User user = this.userMapper.map(userDto);
        VerificationToken verificationToken = new VerificationToken(token, user);
        this.tokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public boolean isEmailExist(String email) {
        return this.userRepository.findByEmail(email) != null;
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = this.userMapper.mapToRegistration(userDto);
        User savedUser = this.userRepository.save(user);
        return this.userMapper.map(savedUser);
    }

    @Override
    @Transactional
    public void update(UserDto userDto) {
        User user = this.userMapper.map(userDto);
        this.userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByLastName(String lastName) {
        List<User> users = this.userRepository.findByLastName(lastName);
        List<UserDto> userDtoList = users.stream().map(this.userMapper::map).toList();
        return userDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        UserDto userDto = this.userMapper.map(user);
        return userDto;
    }

    @Override
    @Transactional
    public void delete(UserDto userDto) {
        userDto.getIncomingQuestions().forEach(q -> this.answerService.delete(q.getAnswer()));
        User user = this.userMapper.map(userDto);
        this.userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = this.userRepository.findAll();
        List<UserDto> userDtoList = users.stream().map(this.userMapper::map).toList();
        return userDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Integer id) {
        User user = this.userRepository.findById(id).orElse(null);
        UserDto userDto = this.userMapper.map(user);
        return userDto;
    }
}
