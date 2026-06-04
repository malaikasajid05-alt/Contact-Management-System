package com.malaika.backend.repository;

import com.malaika.backend.entity.ContactDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactDetailRepository extends JpaRepository<ContactDetail, Long> {

    List<ContactDetail> findByContactId(Long contactId);

    Optional<ContactDetail> findByIdAndContactId(Long id, Long contactId);
}