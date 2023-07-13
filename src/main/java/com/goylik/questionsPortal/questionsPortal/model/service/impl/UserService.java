package com.goylik.questionsPortal.questionsPortal.model.service.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.QuestionDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final IUserMapper userMapper;
    private final IVerificationTokenMapper tokenMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository,
                       IUserMapper userMapper, IVerificationTokenMapper tokenMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.tokenMapper = tokenMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getUserByToken(String token) {
        User user = this.tokenRepository.findUserByToken(token);
        return this.userMapper.map(user);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationTokenDto getVerificationToken(UserDto user) {
        VerificationToken token = this.tokenRepository.findByUser(this.userMapper.map(user));
        VerificationTokenDto tokenDto = this.tokenMapper.map(token);
        return tokenDto;
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationTokenDto getVerificationToken(String verificationToken) {
        VerificationToken token = this.tokenRepository.findByToken(verificationToken);
        VerificationTokenDto tokenDto = this.tokenMapper.map(token);
        return tokenDto;
    }

    @Override
    @Transactional
    public void createVerificationToken(UserDto userDto, String token) {
        User user = this.userMapper.map(userDto);
        VerificationToken oldVerificationToken = this.tokenRepository.findByUser(user);
        VerificationToken verificationToken = new VerificationToken(token, user);
        if (oldVerificationToken != null) {
            verificationToken.setId(oldVerificationToken.getId());
        }

        this.tokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public boolean isEmailExist(String email) {
        return email != null && this.userRepository.findByEmail(email) != null;
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
    @Transactional
    public Page<UserDto> findByLastName(String lastName, Pageable pageable) {
        Page<User> users = this.userRepository.findByLastName(lastName, pageable);
        List<UserDto> userDtoPage = users.stream().map(this.userMapper::map).toList();
        return new PageImpl<>(userDtoPage, pageable, users.getTotalElements());
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
        User user = this.userMapper.map(userDto);
        VerificationToken token = this.tokenRepository.findByUser(user);
        if (token != null) {
            this.tokenRepository.delete(token);
        }

        this.userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> users = this.userRepository.findAll(pageable);
        List<UserDto> userDtoList = users.stream().map(this.userMapper::map).toList();
        return new PageImpl<>(userDtoList, pageable, users.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Integer id) {
        User user = this.userRepository.findById(id).orElse(null);
        UserDto userDto = this.userMapper.map(user);
        return userDto;
    }
}
