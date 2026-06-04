package com.malaika.backend.repository;

import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.enums.DetailType;
import com.malaika.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContactDetailRepositoryTest {

    @Autowired
    private ContactDetailRepository contactDetailRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_contactDetail_success() {

        User user = new User();
        user.setEmail("u@test.com");
        user.setPassword("123");
        user = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setUser(user);
        contact = contactRepository.save(contact);

        ContactDetail detail = new ContactDetail();
        detail.setValue("test@gmail.com");
        detail.setType(DetailType.EMAIL);
        detail.setContact(contact);

        ContactDetail saved = contactDetailRepository.save(detail);

        assertNotNull(saved.getId());
    }

    @Test
    void find_by_contactId_success() {

        User user = new User();
        user.setEmail("u@test.com");
        user.setPassword("123");
        user = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setUser(user);
        contact = contactRepository.save(contact);

        ContactDetail detail = new ContactDetail();
        detail.setValue("1234567890");
        detail.setType(DetailType.PHONE);
        detail.setContact(contact);

        contactDetailRepository.save(detail);

        List<ContactDetail> list =
                contactDetailRepository.findByContactId(contact.getId());

        assertFalse(list.isEmpty());
    }
}