package com.malaika.backend.repository;

import com.malaika.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPnumber(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByPnumber(String pNumber);
}
