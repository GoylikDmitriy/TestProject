package com.goylik.questionsPortal.questionsPortal.model.repository;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByLastName(String lastName);

    Page<User> findByLastName(String lastName, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);
}
