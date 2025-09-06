package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    // Find all folders for a specific category
    List<Folder> findByCategoryOrderByCreatedAtDesc(Category category);

    // Find all folders by category ID
    List<Folder> findByCategory_IdOrderByCreatedAtDesc(Long categoryId);

    // Find folder by name and category
    Optional<Folder> findByNameAndCategory(String name, Category category);

    // Find folder with notes (eager loading)
    @Query("SELECT f FROM Folder f LEFT JOIN FETCH f.notes WHERE f.id = :id")
    Optional<Folder> findByIdWithNotes(@Param("id") Long id);

    // Find all folders for a specific user (through category)
    @Query("SELECT f FROM Folder f WHERE f.category.user.id = :userId ORDER BY f.createdAt DESC")
    List<Folder> findByUserId(@Param("userId") Long userId);

    // Check if folder name exists in category
    boolean existsByNameAndCategory(String name, Category category);

    // Count folders for category
    long countByCategory(Category category);
}
