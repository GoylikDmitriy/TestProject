package com.goylik.questionsPortal.questionsPortal.repository;

import com.goylik.questionsPortal.questionsPortal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional(readOnly = true)
    List<User> findByLastName(String lastName);

    @Transactional(readOnly = true)
    User findByEmail(String email);
}
