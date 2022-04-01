package com.example.management.repository;

import com.example.management.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void findByUsernameAndEmail() {
        String testUsername = "testUsername";
        String testEmail = "testEmail@gmail.com";
        User firstUser = new User(testUsername, testEmail);
        entityManager.persist(firstUser);
        entityManager.flush();

        //when
        User user = userRepository.findByUsernameAndEmail(testUsername, testEmail);
        //then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(firstUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(firstUser.getEmail());
    }
}