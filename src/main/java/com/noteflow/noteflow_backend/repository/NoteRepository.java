package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByFolder(Folder folder);

    List<Note> findByFolderCategoryUser(User user);

    @Query("SELECT n FROM Note n WHERE n.folder.category.user.id = :userId")
    List<Note> findByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM Note n WHERE n.folder.category.user.id = :userId AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Note> searchNotesByUser(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

    @Query("SELECT COUNT(n) FROM Note n WHERE n.folder.category.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}