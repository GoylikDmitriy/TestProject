package com.goylik.questionsPortal.questionsPortal.model.repository;

import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.entity.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Integer> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    @Query("SELECT user FROM VerificationToken token WHERE token.token = :token")
    User findUserByToken(String token);
}
