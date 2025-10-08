package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByCategory(Category category);

    List<Folder> findByCategoryUser(User user);

    boolean existsByNameAndCategory(String name, Category category);

    @Query("SELECT f FROM Folder f LEFT JOIN FETCH f.notes WHERE f.id = :id")
    Optional<Folder> findByIdWithNotes(@Param("id") Long id);
}