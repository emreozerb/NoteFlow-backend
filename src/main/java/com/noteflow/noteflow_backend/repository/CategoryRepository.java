package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find all categories for a specific user
    List<Category> findByUserOrderByCreatedAtDesc(User user);

    // Find all categories by user ID
    List<Category> findByUser_IdOrderByCreatedAtDesc(Long userId);

    // Find category by name and user
    Optional<Category> findByNameAndUser(String name, User user);

    // Find category with folders (eager loading)
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.folders WHERE c.id = :id")
    Optional<Category> findByIdWithFolders(@Param("id") Long id);

    // Check if category name exists for user
    boolean existsByNameAndUser(String name, User user);

    // Count categories for user
    long countByUser(User user);
}
