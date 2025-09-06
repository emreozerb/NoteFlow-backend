package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // // Find user with categories (eager loading)
    // @Query("SELECT u FROM User u LEFT JOIN FETCH u.categories WHERE u.id = :id")
    // Optional<User> findByIdWithCategories(Long id);
}
