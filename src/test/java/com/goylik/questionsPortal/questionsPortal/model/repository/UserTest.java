package com.goylik.questionsPortal.questionsPortal.model.repository;

import com.goylik.questionsPortal.questionsPortal.model.DataJpaTest;
import com.goylik.questionsPortal.questionsPortal.model.entity.Question;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest extends DataJpaTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindAllUsers() {
        List<User> users = this.userRepository.findAll();
        assertThat(users).hasSize(4);
    }

    @Test
    public void shouldFindAllUsersPage() {
        Page<User> users = this.userRepository.findAll(PageRequest.of(0, 3));
        assertThat(users).hasSize(3);

        users = this.userRepository.findAll(PageRequest.of(1, 3));
        assertThat(users).hasSize(1);
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
        Page<User> users = this.userRepository.findByLastName("Cooper", Pageable.unpaged());
        assertThat(users).hasSize(1);
        assertThat(users.get().toList().get(0).getLastName()).isEqualTo("Cooper");

        users = this.userRepository.findByLastName("Coper", Pageable.unpaged());
        assertThat(users).isEmpty();
    }

    @Test
    public void shouldFindUserByLastNamePage() {
        Page<User> users = this.userRepository.findByLastName("Cooper", PageRequest.of(0, 3));
        assertThat(users).hasSize(1);
    }

    @Test
    public void shouldFindUserByEmail() {
        User user = this.userRepository.findByEmail("denny.cooper@mail1.com");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("denny.cooper@mail1.com");

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
}
