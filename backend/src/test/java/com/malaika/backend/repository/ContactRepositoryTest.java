package com.malaika.backend.repository;

import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_contact_success() {

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword("123");
        user = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setUser(user);

        Contact saved = contactRepository.save(contact);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getFirstName());
    }

    @Test
    void findByUserId_success() {

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword("123");
        user = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setUser(user);

        contactRepository.save(contact);

        List<Contact> contacts = contactRepository.findByUserId(user.getId());

        assertFalse(contacts.isEmpty());
    }

    @Test
    void findByIdAndUserId_success() {

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword("123");
        user = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setUser(user);

        Contact saved = contactRepository.save(contact);

        Optional<Contact> found =
                contactRepository.findByIdAndUserId(saved.getId(), user.getId());

        assertTrue(found.isPresent());
    }
}