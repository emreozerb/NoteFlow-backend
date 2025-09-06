package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.repository.NoteRepository;
import com.noteflow.noteflow_backend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private FolderRepository folderRepository;

    // Create new note
    public Note createNote(Long folderId, Long userId, Note note) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        // Check user ownership of folder
        if (!folder.getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Folder belongs to different user");
        }

        note.setFolder(folder);
        return noteRepository.save(note);
    }

    // Get note by ID (with user validation)
    public Optional<Note> getNoteById(Long id, Long userId) {
        Optional<Note> note = noteRepository.findById(id);

        if (note.isPresent() && !note.get().getFolder().getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Note belongs to different user");
        }

        return note;
    }

    // Get all notes for user
    public List<Note> getNotesByUser(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    // Search notes for user
    public List<Note> searchNotes(Long userId, String searchTerm) {
        return noteRepository.searchNotesByUser(userId, searchTerm);
    }

    // Update note
    public Note updateNote(Long id, Long userId, Note updatedNote) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // Check user ownership
        if (!note.getFolder().getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Note belongs to different user");
        }

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        return noteRepository.save(note);
    }

    // Move note to different folder
    public Note moveNote(Long noteId, Long newFolderId, Long userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        Folder newFolder = folderRepository.findById(newFolderId)
                .orElseThrow(() -> new RuntimeException("Target folder not found"));

        // Check user ownership of both note and target folder
        if (!note.getFolder().getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Note belongs to different user");
        }

        if (!newFolder.getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Target folder belongs to different user");
        }

        note.setFolder(newFolder);
        return noteRepository.save(note);
    }

    // Delete note
    public void deleteNote(Long id, Long userId) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // Check user ownership
        if (!note.getFolder().getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Note belongs to different user");
        }

        noteRepository.deleteById(id);
    }

    // Count notes for user
    public long countNotesByUser(Long userId) {
        return noteRepository.countByUserId(userId);
    }
}