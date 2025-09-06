package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Find all notes for a specific folder
    List<Note> findByFolderOrderByCreatedAtDesc(Folder folder);

    // Find all notes by folder ID
    List<Note> findByFolder_IdOrderByCreatedAtDesc(Long folderId);

    // Find all notes for a specific user (through folder -> category -> user)
    @Query("SELECT n FROM Note n WHERE n.folder.category.user.id = :userId ORDER BY n.createdAt DESC")
    List<Note> findByUserId(@Param("userId") Long userId);

    // Find notes by title containing text (case insensitive)
    @Query("SELECT n FROM Note n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Note> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Find notes by content containing text (case insensitive)
    @Query("SELECT n FROM Note n WHERE LOWER(n.content) LIKE LOWER(CONCAT('%', :content, '%'))")
    List<Note> findByContentContainingIgnoreCase(@Param("content") String content);

    // Search notes by title or content for a specific user
    @Query("SELECT n FROM Note n WHERE n.folder.category.user.id = :userId AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY n.createdAt DESC")
    List<Note> searchNotesByUser(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

    // Count notes for user
    @Query("SELECT COUNT(n) FROM Note n WHERE n.folder.category.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}