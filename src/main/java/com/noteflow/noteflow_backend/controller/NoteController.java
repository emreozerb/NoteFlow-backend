package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.dto.request.note.CreateNoteRequestDTO;
import com.noteflow.noteflow_backend.dto.request.note.UpdateNoteRequestDTO;
import com.noteflow.noteflow_backend.dto.response.NoteDTO;
import com.noteflow.noteflow_backend.mapper.NoteMapper;
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

    @Autowired
    private NoteMapper noteMapper;

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNotes(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Note> notes = noteService.getNotesByUser(user);
        List<NoteDTO> noteDTOs = noteMapper.toDTOList(notes);
        return ResponseEntity.ok(noteDTOs);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteById(@PathVariable Long noteId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.getNoteById(noteId, user);
            NoteDTO noteDTO = noteMapper.toDTO(note);
            return ResponseEntity.ok(noteDTO);
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
            List<NoteDTO> noteDTOs = noteMapper.toDTOList(notes);
            return ResponseEntity.ok(noteDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchNotes(@RequestParam String q, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<Note> notes = noteService.searchNotes(user, q);
            List<NoteDTO> noteDTOs = noteMapper.toDTOList(notes);
            return ResponseEntity.ok(noteDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody CreateNoteRequestDTO request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.createNote(
                    request.getTitle(),
                    request.getContent(),
                    request.getFolderId(),
                    user);
            NoteDTO noteDTO = noteMapper.toDTO(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(noteDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable Long noteId,
            @Valid @RequestBody UpdateNoteRequestDTO request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Note note = noteService.updateNote(
                    noteId,
                    request.getTitle(),
                    request.getContent(),
                    user);
            NoteDTO noteDTO = noteMapper.toDTO(note);
            return ResponseEntity.ok(noteDTO);
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
            NoteDTO noteDTO = noteMapper.toDTO(note);
            return ResponseEntity.ok(noteDTO);
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

}