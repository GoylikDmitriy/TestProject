package com.goylik.questionsPortal.questionsPortal.service;

import com.goylik.questionsPortal.questionsPortal.model.User;
import com.goylik.questionsPortal.questionsPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(User user) {
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
