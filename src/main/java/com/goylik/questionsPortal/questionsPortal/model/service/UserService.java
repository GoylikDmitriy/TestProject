package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnswerService answerService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AnswerService answerService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.answerService = answerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean isUserExist(User user) {
        return this.userRepository.findByEmail(user.getEmail()) != null;
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        this.userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findByLastName(String lastName) {
        return this.userRepository.findByLastName(lastName);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Transactional
    public void delete(User user) {
        user.getIncomingQuestions().forEach(q -> this.answerService.delete(q.getAnswer()));
        this.userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(int id) {
        return this.userRepository.findById(id).orElse(null);
    }
}
