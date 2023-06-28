package com.goylik.questionsPortal.questionsPortal.model.service;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findById(Integer id);
    void save(User user);
    void update(User user);
    void delete(User user);
    List<User> findByLastName(String lastName);
    User findByEmail(String email);
    boolean isEmailExist(String email);
}
