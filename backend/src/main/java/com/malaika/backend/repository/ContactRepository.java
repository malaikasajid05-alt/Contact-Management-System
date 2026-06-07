package com.malaika.backend.repository;

import com.malaika.backend.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserId(Long userId);
    Page<Contact> findByUserId(Long userId, Pageable pageable);
    Optional<Contact> findByIdAndUserId(Long id, Long userId);
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(c.lastName)  LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(c.title)     LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Contact> searchByUserId(@Param("userId") Long userId, @Param("q") String q);
}
