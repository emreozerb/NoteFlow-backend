package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.ImageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageAttachmentRepository extends JpaRepository<ImageAttachment, Long> {

    List<ImageAttachment> findByNote_NoteIdOrderByDisplayOrderAsc(Long noteId);

    void deleteByNote_NoteId(Long noteId);

}
