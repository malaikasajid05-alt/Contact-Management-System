package com.malaika.backend.repository;

import com.malaika.backend.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findByContactId(Long contactId);

    Optional<Email> findByIdAndContactId(Long id, Long contactId);
}