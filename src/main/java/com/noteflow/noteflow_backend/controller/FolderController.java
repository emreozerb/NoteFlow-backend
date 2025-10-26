package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.dto.request.folder.CreateFolderRequestDTO;
import com.noteflow.noteflow_backend.dto.request.folder.UpdateFolderRequestDTO;
import com.noteflow.noteflow_backend.dto.response.FolderDTO;
import com.noteflow.noteflow_backend.mapper.FolderMapper;
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

    @Autowired
    private FolderMapper folderMapper;

    @GetMapping
    public ResponseEntity<List<FolderDTO>> getAllFolders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Folder> folders = folderService.getFoldersByUser(user);
        List<FolderDTO> folderDTOs = folderMapper.toDTOList(folders);
        return ResponseEntity.ok(folderDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFolderById(@PathVariable Long id, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Folder folder = folderService.getFolderById(id, user);
            FolderDTO folderDTO = folderMapper.toDTO(folder);
            return ResponseEntity.ok(folderDTO);
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
            List<FolderDTO> folderDTOs = folderMapper.toDTOList(folders);
            return ResponseEntity.ok(folderDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createFolder(@Valid @RequestBody CreateFolderRequestDTO request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Folder folder = folderService.createFolder(
                    request.getName(),
                    request.getDescription(),
                    request.getCategoryId(),
                    user);
            FolderDTO folderDTO = folderMapper.toDTO(folder);
            return ResponseEntity.status(HttpStatus.CREATED).body(folderDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Long id,
            @Valid @RequestBody UpdateFolderRequestDTO request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Folder folder = folderService.updateFolder(
                    id,
                    request.getName(),
                    request.getDescription(),
                    user);
            FolderDTO folderDTO = folderMapper.toDTO(folder);
            return ResponseEntity.ok(folderDTO);
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

}