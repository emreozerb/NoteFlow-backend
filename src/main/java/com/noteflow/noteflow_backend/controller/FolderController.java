package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin(origins = "*")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping
    public ResponseEntity<List<Folder>> getAllFolders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Folder> folders = folderService.getFoldersByUser(user);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFolderById(@PathVariable Long id, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Folder folder = folderService.getFolderById(id, user);
            return ResponseEntity.ok(folder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getFoldersByCategory(@PathVariable Long categoryId,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<Folder> folders = folderService.getFoldersByCategory(categoryId, user);
            return ResponseEntity.ok(folders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createFolder(@Valid @RequestBody CreateFolderRequest request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Folder folder = folderService.createFolder(
                    request.getName(),
                    request.getDescription(),
                    request.getCategoryId(),
                    user);
            return ResponseEntity.status(HttpStatus.CREATED).body(folder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Long id,
            @Valid @RequestBody UpdateFolderRequest request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Folder folder = folderService.updateFolder(
                    id,
                    request.getName(),
                    request.getDescription(),
                    user);
            return ResponseEntity.ok(folder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            folderService.deleteFolder(id, user);
            return ResponseEntity.ok("Folder deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Request DTOs
    public static class CreateFolderRequest {
        private String name;
        private String description;
        private Long categoryId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    }

    public static class UpdateFolderRequest {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}