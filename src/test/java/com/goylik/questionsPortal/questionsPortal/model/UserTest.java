package com.goylik.questionsPortal.questionsPortal.model;

import com.goylik.questionsPortal.questionsPortal.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    @Autowired
    private UserService userService;

    @Test
    public void shouldReceiveQuestions() {
        User user = this.userService.findById(1);
        List<Question> incoming = user.getIncomingQuestions();
        List<Question> outgoing = user.getOutgoingQuestions();
        assertThat(incoming).hasSize(1);
        assertThat(outgoing).hasSize(2);
    }

    @Test
    public void shouldFindAllUsers() {
        List<User> users = this.userService.findAll();
        assertThat(users).hasSize(4);
    }

    @Test
    public void shouldFindUserById() {
        User user = this.userService.findById(1);
        assertThat(user).isNotNull();

        user = this.userService.findById(999);
        assertThat(user).isNull();
    }

    @Test
    public void shouldFindUserByLastName() {
        List<User> users = this.userService.findByLastName("Cooper");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getLastName()).isEqualTo("Cooper");

        users = this.userService.findByLastName("Coper");
        assertThat(users).isEmpty();
    }

    @Test
    public void shouldFindUserByEmail() {
        User user = this.userService.findByEmail("denny.cooper@gmail.com");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("denny.cooper@gmail.com");

        user = this.userService.findByEmail("de.pe@gmail.com");
        assertThat(user).isNull();
    }

    @Test
    public void shouldInsertUser() {
        User user = new User("Jack", "Lipa", "jack.lipa@gmail.com", "124423412", "1111111");
        this.userService.save(user);
        List<User> users = this.userService.findAll();
        assertThat(users).hasSize(5);
        User savedUser = users.get(users.size() - 1);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void shouldUpdateUser() {
        User user = this.userService.findById(1);
        user.setFirstName("Jack");
        this.userService.update(user);
        user = this.userService.findById(1);
        assertThat(user.getFirstName()).isEqualTo("Jack");
    }

    @Test
    public void shouldDeleteUser() {
        User user = this.userService.findById(1);
        this.userService.delete(user);
        List<User> users = this.userService.findAll();
        user = this.userService.findById(1);
        assertThat(users).hasSize(3);
        assertThat(user).isNull();
    }
}
