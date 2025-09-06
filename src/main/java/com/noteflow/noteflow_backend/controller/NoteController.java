package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // Create new note
    @PostMapping("/folder/{folderId}/user/{userId}")
    public ResponseEntity<?> createNote(@PathVariable Long folderId, @PathVariable Long userId,
            @Valid @RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(folderId, userId, note);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get note by ID
    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Optional<Note> note = noteService.getNoteById(id, userId);
            if (note.isPresent()) {
                return ResponseEntity.ok(note.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all notes for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotesByUser(@PathVariable Long userId) {
        try {
            List<Note> notes = noteService.getNotesByUser(userId);
            return ResponseEntity.ok(notes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Search notes
    @GetMapping("/search/user/{userId}")
    public ResponseEntity<?> searchNotes(@PathVariable Long userId, @RequestParam String q) {
        try {
            List<Note> notes = noteService.searchNotes(userId, q);
            return ResponseEntity.ok(notes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update note
    @PutMapping("/{id}/user/{userId}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @PathVariable Long userId,
            @Valid @RequestBody Note updatedNote) {
        try {
            Note note = noteService.updateNote(id, userId, updatedNote);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Move note to different folder
    @PutMapping("/{noteId}/move/folder/{folderId}/user/{userId}")
    public ResponseEntity<?> moveNote(@PathVariable Long noteId, @PathVariable Long folderId,
            @PathVariable Long userId) {
        try {
            Note note = noteService.moveNote(noteId, folderId, userId);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete note
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, @PathVariable Long userId) {
        try {
            noteService.deleteNote(id, userId);
            return ResponseEntity.ok("Note deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Count notes for user
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<?> countNotesByUser(@PathVariable Long userId) {
        try {
            long count = noteService.countNotesByUser(userId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}