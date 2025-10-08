package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Note> notes = noteService.getNotesByUser(user);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteById(@PathVariable Long noteId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.getNoteById(noteId, user);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<?> getNotesByFolder(@PathVariable Long folderId,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<Note> notes = noteService.getNotesByFolder(folderId, user);
            return ResponseEntity.ok(notes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchNotes(@RequestParam String q, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<Note> notes = noteService.searchNotes(user, q);
            return ResponseEntity.ok(notes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody CreateNoteRequest request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.createNote(
                    request.getTitle(),
                    request.getContent(),
                    request.getFolderId(),
                    user);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable Long noteId,
            @Valid @RequestBody UpdateNoteRequest request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.updateNote(
                    noteId,
                    request.getTitle(),
                    request.getContent(),
                    user);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{noteId}/move/{folderId}")
    public ResponseEntity<?> moveNote(@PathVariable Long noteId,
            @PathVariable Long folderId,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.moveNote(noteId, folderId, user);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            noteService.deleteNote(noteId, user);
            return ResponseEntity.ok("Note deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countNotes(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            long count = noteService.countNotesByUser(user);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Request DTOs
    public static class CreateNoteRequest {
        private String title;
        private String content;
        private Long folderId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getFolderId() {
            return folderId;
        }

        public void setFolderId(Long folderId) {
            this.folderId = folderId;
        }
    }

    public static class UpdateNoteRequest {
        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}