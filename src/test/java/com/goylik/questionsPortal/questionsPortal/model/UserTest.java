package com.goylik.questionsPortal.questionsPortal.model;

import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import com.goylik.questionsPortal.questionsPortal.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TypeExcludeFilters({org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter.class})
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReceiveQuestions() {
        User user = this.userRepository.findById(1).orElse(null);
        List<Question> incoming = user.getIncomingQuestions();
        List<Question> outgoing = user.getOutgoingQuestions();
        assertThat(incoming).hasSize(1);
        assertThat(outgoing).hasSize(2);
    }

    @Test
    public void shouldFindAllUsers() {
        List<User> users = this.userRepository.findAll();
        assertThat(users).hasSize(4);
    }

    @Test
    public void shouldFindUserById() {
        User user = this.userRepository.findById(1).orElse(null);
        assertThat(user).isNotNull();

        user = this.userRepository.findById(999).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    public void shouldFindUserByLastName() {
        List<User> users = this.userRepository.findByLastName("Cooper");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getLastName()).isEqualTo("Cooper");

        users = this.userRepository.findByLastName("Coper");
        assertThat(users).isEmpty();
    }

    @Test
    public void shouldFindUserByEmail() {
        User user = this.userRepository.findByEmail("denny.cooper@gmail.com");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("denny.cooper@gmail.com");

        user = this.userRepository.findByEmail("de.pe@gmail.com");
        assertThat(user).isNull();
    }

    @Test
    @Transactional
    public void shouldInsertUser() {
        User user = new User("Jack", "Lipa", "jack.lipa@gmail.com", "124423412", "1111111");
        this.userRepository.save(user);
        List<User> users = this.userRepository.findAll();
        assertThat(users).hasSize(5);
        User savedUser = users.get(users.size() - 1);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    @Transactional
    public void shouldUpdateUser() {
        User user = this.userRepository.findById(1).orElse(null);
        user.setFirstName("Jack");
        this.userRepository.save(user);
        user = this.userRepository.findById(1).orElse(null);
        assertThat(user.getFirstName()).isEqualTo("Jack");
    }

    @Test
    @Transactional
    public void shouldDeleteUser() {
        User user = this.userRepository.findById(1).orElse(null);
        this.userRepository.delete(user);
        List<User> users = this.userRepository.findAll();
        user = this.userRepository.findById(1).orElse(null);
        assertThat(users).hasSize(3);
        assertThat(user).isNull();
    }
}
