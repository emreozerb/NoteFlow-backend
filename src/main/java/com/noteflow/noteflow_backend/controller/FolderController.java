package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin(origins = "*")
public class FolderController {

    @Autowired
    private FolderService folderService;

    // Create new folder
    @PostMapping("/category/{categoryId}/user/{userId}")
    public ResponseEntity<?> createFolder(@PathVariable Long categoryId, @PathVariable Long userId,
            @Valid @RequestBody Folder folder) {
        try {
            Folder createdFolder = folderService.createFolder(categoryId, userId, folder);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFolder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get folder by ID
    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<?> getFolderById(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Optional<Folder> folder = folderService.getFolderById(id, userId);
            if (folder.isPresent()) {
                return ResponseEntity.ok(folder.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get folder with notes
    @GetMapping("/{id}/with-notes/user/{userId}")
    public ResponseEntity<?> getFolderWithNotes(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Optional<Folder> folder = folderService.getFolder(id, userId);
            if (folder.isPresent()) {
                return ResponseEntity.ok(folder.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all folders for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFoldersByUser(@PathVariable Long userId) {
        try {
            List<Folder> folders = folderService.getFoldersByUser(userId);
            return ResponseEntity.ok(folders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete folder
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id, @PathVariable Long userId) {
        try {
            folderService.deleteFolder(id, userId);
            return ResponseEntity.ok("Folder deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
