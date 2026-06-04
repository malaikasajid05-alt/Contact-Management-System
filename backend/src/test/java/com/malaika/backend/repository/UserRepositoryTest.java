package com.malaika.backend.repository;

import com.malaika.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_user_success() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123");

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("test@test.com", saved.getEmail());
    }

    @Test
    void findByEmail_success() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123");

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("test@test.com");

        assertTrue(found.isPresent());
    }
}