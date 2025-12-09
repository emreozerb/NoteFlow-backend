package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.dto.response.ImageAttachmentDTO;
import com.noteflow.noteflow_backend.mapper.ImageAttachmentMapper;
import com.noteflow.noteflow_backend.model.ImageAttachment;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.service.ImageAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageAttachmentService imageAttachmentService;

    @Autowired
    private ImageAttachmentMapper imageAttachmentMapper;

    @PostMapping("/{noteId}/images")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long noteId,
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "displayOrder", required = false, defaultValue = "0") Integer displayOrder,
            Authentication authentication,
            HttpServletRequest request) {
        try {
            User user = (User) authentication.getPrincipal();
            ImageAttachment imageAttachment = imageAttachmentService.uploadImage(noteId, file, displayOrder, user);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            ImageAttachmentDTO imageDTO = imageAttachmentMapper.toDTO(imageAttachment, baseUrl);

            return ResponseEntity.status(HttpStatus.CREATED).body(imageDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{noteId}/images")
    public ResponseEntity<?> getImagesByNote(
            @PathVariable Long noteId,
            Authentication authentication,
            HttpServletRequest request) {
        try {
            User user = (User) authentication.getPrincipal();
            List<ImageAttachment> images = imageAttachmentService.getImagesByNote(noteId, user);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            List<ImageAttachmentDTO> imageDTOs = imageAttachmentMapper.toDTOList(images, baseUrl);

            return ResponseEntity.ok(imageDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{noteId}/images/{imageId}")
    public ResponseEntity<?> downloadImage(
            @PathVariable Long noteId,
            @PathVariable Long imageId,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Resource resource = imageAttachmentService.loadImageAsResource(noteId, imageId, user);

            String contentType = "application/octet-stream";
            try {
                contentType = resource.getURL().openConnection().getContentType();
            } catch (IOException ex) {
                // Use default content type
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error loading file: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{noteId}/images/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long noteId,
            @PathVariable Long imageId,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            imageAttachmentService.deleteImage(noteId, imageId, user);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting file: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
